package ru.albina.planner.service.planner.scheduler;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.albina.planner.dto.planner.*;
import ru.albina.planner.dto.reference.WeekNumberResult;
import ru.albina.planner.dto.response.data.DoctorDayDto;
import ru.albina.planner.dto.response.data.TaskDayDto;
import ru.albina.planner.mapper.ModalityMapper;

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
public class DistributorServiceV1 implements DistributorService {

    /*
    Правила
        В неделе 40 часов
        На день в неделю
     */

    @Override
    public Map<LocalDate, List<DoctorDayDto>> distributeDoctors(PlannerDto planner) {
        final var doctors = planner.getDoctors().stream().collect(Collectors.toMap(DoctorDto::getId, Function.identity()));
        planner.setDoctors(
                planner.getDoctors().stream().sorted((doctor1, doctor2) -> {
                    int sum1 = doctor1.getModality().size() + doctor1.getOptionalModality().size();
                    int sum2 = doctor2.getModality().size() + doctor2.getOptionalModality().size();
                    return Integer.compare(sum1, sum2);
                }).collect(Collectors.toList())
        );

        final var month = new Month(planner.getMonth(), planner.getMonthlyHours(), planner.getWeekNumbers(), planner.getWorkload());


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
                //log.info("Doctor {} can work at {}", doctor.getId(), possibleDayOfWeek);
                for (LocalDate localDate : possibleDayOfWeek) {
                    if (workingWeek.isOverhead(doctor.getId())) {
                        break;
                    }
                    if (!workingWeek.willBeHasWeekends(doctor.getId(), localDate)) {
                        continue;
                    }
                    final var activities = this.days(workingWeek, doctor);
                    for (final var modalityToHours : activities.entrySet()) {
                        final var availableHours =
                                Math.min(
                                        month.getAvailableHours(doctor),
                                        Math.min(workingWeek.getAvailableHours(doctor), modalityToHours.getValue())
                                );

                        if (availableHours <= 0d) {
                            break;
                        }

                        month.addWork(doctor, workingWeek, localDate, modalityToHours.getKey(), availableHours);
                        if (workingWeek.isOverhead(doctor.getId())) {
                            break;
                        }
                    }
                }
            }
        }

        return this.mapMonth(month);
    }

    private Map<LocalDate, List<DoctorDayDto>> mapMonth(Month month) {
        return month.getWeeks().values().stream().map(Week::getDays)
                .map(Map::values)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Day::getDate, v -> this.map(v.getDoctors())));
    }

    private List<DoctorDayDto> map(Map<UUID, DoctorDay> doctors) {

        return doctors.values().stream().map(doctorDay ->
                        DoctorDayDto.builder()
                                .id(doctorDay.getDoctorId())
                                .tasks(
                                        doctorDay.getModalityWorkload().entrySet().stream().map(entry -> {
                                            final var modalityContainer = ModalityMapper.from(entry.getKey());
                                            return TaskDayDto.builder()
                                                    .modality(modalityContainer.modality())
                                                    .typeModality(modalityContainer.typeModality())
                                                    .hours(entry.getValue())
                                                    .build();
                                        }).toList()
                                )
                                .build())
                .toList();
    }

    public Map<String, Double> days(Week week, DoctorDto doctor) {
        var hours = doctor.getHours();
        final var modalityPriority = new ArrayList<>(doctor.getModality());

        modalityPriority.addAll(doctor.getOptionalModality());
        final var result = new HashMap<String, Double>();
        for (String s : modalityPriority) {
            final var load = week.getWorkloads().get(s);
            if (load == null) {
                continue;
            }
            final var performance = doctor.getPerformances().get(s);

            if (load <= 0) {
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
                .filter(localDate -> this.isAvailable(localDate, doctor))
                .toList();
    }

    private boolean isAvailable(LocalDate localDate, DoctorDto doctorDto) {
        final var startDate = Optional.ofNullable(doctorDto.getStartContract()).orElse(LocalDate.MAX);
        final var endDate = Optional.ofNullable(doctorDto.getEndContract()).orElse(LocalDate.MAX);

        return !localDate.isBefore(
                startDate
        ) && !localDate.isAfter(
                endDate
        ) && !localDate.isEqual(
                endDate
        );
    }

    @Getter
    @NoArgsConstructor
    static class Month {
        private LocalDate month;
        private Double monthDouble;
        private Map<UUID, Double> hours = new HashMap<>();
        private Map<Integer, Week> weeks;

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
            return this.hours.getOrDefault(doctorDto.getId(), 0d) > this.calculateHours(doctorDto);
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
                log.warn("{} has more than {} at {}", doctorId, this.calculateHours(doctorDto), this.month);
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
    @NoArgsConstructor
    static class Week {
        private LocalDate start;
        private final Map<UUID, Double> hours = new HashMap<>();

        private final Map<LocalDate, Day> days = new HashMap<>();

        private final Map<String, Long> workloads = new HashMap<>();


        Week(LocalDate start, LocalDate end, List<WorkloadDto> workloads) {
            this.start = start;
            do {
                days.put(
                        start,
                        Day.builder()
                                .date(start)
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

        public boolean willBeHasWeekends(UUID id, LocalDate localDate) {
            final var weekendsDoctor = this.days.values().stream()
                    .filter(day -> day.getDoctors().get(id) == null && !day.getDate().isEqual(localDate))
                    .map(Day::getDate)
                    .sorted()
                    .toList();
            for (int i = 0; i < weekendsDoctor.size() - 1; i++) {
                LocalDate date1 = weekendsDoctor.get(i);
                LocalDate date2 = weekendsDoctor.get(i + 1);

                if (date1.plusDays(1).equals(date2)) {
                    return true;
                }
            }
            return false;
        }

        public Day getDay(LocalDate date) {
            return Objects.requireNonNull(this.days.get(date));
        }

        public double getHours(UUID doctorId) {
            return this.hours.getOrDefault(doctorId, 0d);
        }

        public boolean isOverhead(UUID doctorId) {
            return this.hours.getOrDefault(doctorId, 0d) > 40;
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
            final var performance = (long) Math.ceil(hours * doctor.getPerformances().get(modality));
            workloads.put(modality, workloads.get(modality) - performance);
            day.addWork(doctor.getId(), modality, hours);
            this.addHours(doctor.getId(), hours);
        }

        public double getAvailableHours(DoctorDto doctor) {
            return Math.max(0, 40 - this.getHours(doctor.getId()));
        }
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    static class Day {
        private LocalDate date;
        private final Map<UUID, DoctorDay> doctors = new HashMap<>();

        public void addWork(UUID doctorId, String modality, double hours) {
            if (!doctors.containsKey(doctorId)) {
                doctors.put(doctorId, DoctorDay.builder().doctorId(doctorId).build());
            }
            doctors.get(doctorId).addWork(modality, hours);
        }
    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    static class DoctorDay {
        private UUID doctorId;
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
