package ru.albina.planner.service;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.albina.planner.dto.planner.*;
import ru.albina.planner.dto.reference.WeekNumberResult;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributorService {

    /*
    Правила
        В неделе 40 часов
        На день в неделю
     */

    private static final Double RATE = 1d;

    public Map<LocalDate, DoctorDto> distributeDoctors(PlannerDto planner) {

        final var hours = new HashMap<UUID, Long>();
        final var doctors = planner.getDoctors().stream().collect(Collectors.toMap(DoctorDto::getId, Function.identity()));


        final var month = new Month(LocalDate.of(2023, 6, 1), planner.getMonthlyHours(), planner.getWeekNumbers(), planner.getWorkload());


        //Init prev
        for (ScheduleDto scheduleDto : planner.getSchedule()) {
            final var week = month.getWeek(scheduleDto.getWeekNumber());
            for (DoctorScheduleDto doctor : scheduleDto.getDoctors()) {
                final var id = doctor.getId();
                for (DayDto day : doctor.getDays()) {
                    for (TaskDto task : day.getTasks()) {
                        month.addWork(doctors.get(id), week, day.getDate(), task.getModality(), task.getHours());
                    }
                }
            }
        }


        for (final var week : planner.getWeekNumbers()) {
            for (final var doctor : planner.getDoctors()) {
                if (month.isOverhead(doctor)) {
                    continue;
                }
                final var workingWeek = month.getWeek(week.getWeekNumber());
                final var possibleDayOfWeek = this.findDay(workingWeek, doctor);

                for (LocalDate localDate : possibleDayOfWeek) {
                    if (workingWeek.isOverhead(doctor.getId())) {
                        break;
                    }
                    final var workingDay = workingWeek.getDay(localDate);
                    final var activities = this.days(workingDay, doctor);
                    for (final var modalityToHours : activities.entrySet()) {
                        final var availableHours =
                                Math.min(
                                        month.getAvailableHours(doctor),
                                        Math.min(workingWeek.getAvailableHours(doctor), modalityToHours.getValue())
                                );

                        if (availableHours <= 0d) {
                            break;
                        }

                        month.addWork(doctor, workingWeek, localDate, modalityToHours.getKey(), modalityToHours.getValue());
                        if (workingWeek.isOverhead(doctor.getId())) {
                            break;
                        }
                    }
                }

//                final var mainModality = doctor.getPerformances()
//                workingWeek.getWorkloads().
            }
        }


        for (final var doctor : planner.getDoctors()) {

        }


        return Map.of();
    }

    public Map<String, Double> days(Day day, DoctorDto doctor) {
        var hours = doctor.getHours();
        final var modalityPriority = new ArrayList<>(doctor.getModality());

        modalityPriority.addAll(doctor.getOptionalModality());
        final var result = new HashMap<String, Double>();
        for (String s : modalityPriority) {
            final var load = day.getWorkloads().get(s);
            final var performance = doctor.getPerformances().get(s);

            if (load == 0) {
                continue;
            }
            final var hR = Math.min(hours, this.hours(load, performance, hours));

            hours -= hR;
            result.put(s, hR);

            if (hours <= 0) {
                break;
            }
        }
        return result;
    }

    public Double hours(long load, long performance, double maxHours) {
        var hours = 0d;
        //Not quite perform
        while (load > 0 && hours <= maxHours) {
            if (load >= performance) {
                hours++;
            } else {
                hours += new BigDecimal(1 / 5d, new MathContext(1, RoundingMode.HALF_DOWN)).doubleValue();
            }
            load -= performance;
        }

        return hours;
    }

    public List<LocalDate> findDay(Week week, DoctorDto doctor) {
        final var all = IntStream.range(1, 8).boxed().toList();
        final var days = new ArrayList<>(Optional.ofNullable(doctor.getWorkDays()).orElse(all));

        for (Integer i : all) {
            if (!days.contains(i)) {
                days.add(i);
            }
        }
        //gets days per priority
        return days.stream().map(DayOfWeek::of)
                .map(dayOfWeek -> week.getStart().plusDays(dayOfWeek.getValue() - 1))
                .filter(localDate -> !doctor.getAbsenceSchedules().contains(localDate))
                .toList();
    }


    static class Month {
        private final LocalDate month;
        private final Double monthDouble;
        private final Map<UUID, Double> hours = new HashMap<>();
        private final Map<Integer, Week> weeks;

        public Month(LocalDate month, Double monthDouble, List<WeekNumberResult> weekNumbers, List<WorkloadDto> workload) {
            this.month = month.withDayOfMonth(1);
            this.monthDouble = monthDouble;
            this.weeks = weekNumbers.stream()
                    .collect(Collectors.toMap(
                            WeekNumberResult::getWeekNumber, weekNumberResult -> new Week(
                                    weekNumberResult.getStartDate(),
                                    weekNumberResult.getEndDate(),
                                    workload.stream().filter(workloadDto -> workloadDto.getWeek() == weekNumberResult.getWeekNumber()).toList()
                            ))
                    );
        }


        public Week getWeek(int weekNumber) {
            return Objects.requireNonNull(weeks.get(weekNumber));
        }

        public double getHours(UUID doctorId) {
            return this.hours.getOrDefault(doctorId, 0d);
        }

        public boolean isOverhead(DoctorDto doctorDto) {
            return this.hours.getOrDefault(doctorDto.getId(), 0d) >= this.calculateHours(doctorDto);
        }

        public void addHours(DoctorDto doctorDto, LocalDate date, double hours) {
            if (!month.isEqual(date.withDayOfMonth(1))) {
                return;
            }
            final var doctorId = doctorDto.getId();
            if (this.hours.containsKey(doctorId)) {
                this.hours.put(doctorId, this.hours.get(doctorId) + hours);
            } else {
                this.hours.put(doctorId, hours);
            }

            if (this.isOverhead(doctorDto)) {
                log.warn("{} has more than {}", doctorId, this.month);
            }
        }

        public void addWork(DoctorDto doctor, Week week, LocalDate date, String modality, Double hours) {
            week.addWork(doctor, date, modality, hours);
            this.addHours(doctor, date, hours);
        }

        private double calculateHours(DoctorDto doctorDto) {
            return this.monthDouble * doctorDto.getRate();
        }

        public double getAvailableHours(DoctorDto doctor) {
            return Math.max(0, this.calculateHours(doctor) - this.getHours(doctor.getId()));
        }
    }

    @Getter
    static class Week {
        private final LocalDate start;
        private final Map<UUID, Double> hours = new HashMap<>();

        private final Map<LocalDate, Day> days = new HashMap<>();

        private final Map<String, Long> workloads = new HashMap<>();


        Week(LocalDate start, LocalDate end, List<WorkloadDto> workloads) {
            this.start = start;
            final var mapWorkload = new HashMap<String, long[]>();
            for (WorkloadDto workload : workloads) {
                mapWorkload.put(workload.getModality(), splitNumber(workload.getValue(), 7));
            }
            int i = 0;
            do {
                days.put(
                        start,
                        Day.builder()
                                .date(start)
                                .workloads(mapWorkload.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue()[i])))
                                .build()
                );
                start = start.plusDays(1);

            } while (!start.isAfter(end));

            //INit
            for (WorkloadDto workload : workloads) {
                if (this.workloads.containsKey(workload.getModality())) {
                    throw new RuntimeException("Duplicate workload modality: " + workload.getModality());
                } else {
                    this.workloads.put(workload.getModality(), workload.getValue());
                }
            }

        }

        //  public boolean can

        public static long[] splitNumber(long number, int parts) {
            long[] result = new long[parts];

            // Заполняем массив
            long base = number / parts;
            long remainder = number % parts;

            for (int i = 0; i < parts; i++) {
                result[i] = base;
                if (i < remainder) {
                    result[i]++;
                }
            }

            return result;
        }


        public Day getDay(LocalDate date) {
            return Objects.requireNonNull(this.days.get(date));
        }

        public double getHours(UUID doctorId) {
            return this.hours.getOrDefault(doctorId, 0d);
        }

        public boolean isOverhead(UUID doctorId) {
            return this.hours.getOrDefault(doctorId, 0d) >= 40;
        }

        public void addHours(UUID doctorId, double hours) {
            if (this.hours.containsKey(doctorId)) {
                this.hours.put(doctorId, this.hours.get(doctorId) + hours);
            } else {
                this.hours.put(doctorId, hours);
            }

            if (this.isOverhead(doctorId)) {
                log.warn("{} has more than 40", doctorId);
            }
        }

        public void addWork(DoctorDto doctor, LocalDate date, String modality, Double hours) {
            final var day = this.getDay(date);
            final var performance = Math.round(hours * doctor.getPerformances().get(modality));
            day.addWork(doctor.getId(), modality, performance, hours);
            this.addHours(doctor.getId(), hours);
        }

        public double getAvailableHours(DoctorDto doctor) {
            return Math.max(0, 40 - this.getHours(doctor.getId()));
        }
    }

    @Getter
    @Builder
    static class Day {
        private final LocalDate date;
        private final Map<UUID, DoctorDay> doctors = new HashMap<>();
        private final Map<String, Long> workloads;

        public void addWork(UUID doctorId, String modality, long performance, double hours) {
            workloads.put(modality, workloads.get(modality) - performance);
            if (!doctors.containsKey(doctorId)) {
                doctors.put(doctorId, DoctorDay.builder().build());
            }
            doctors.get(doctorId).addWork(modality, hours);
        }
    }

    @Builder
    static class DoctorDay {
        //hours
        private final Map<String, Double> modalityWorkload = new HashMap<>();


        public void addWork(String modality, double hours) {
            if (modalityWorkload.containsKey(modality)) {
                modalityWorkload.put(modality, modalityWorkload.get(modality) + hours);
            } else {
                modalityWorkload.put(modality, hours);
            }
        }
    }
}
