package ru.albina.planner.service.planner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.albina.planner.domain.DoctorScheduleEntity;
import ru.albina.planner.domain.WorkScheduleEntity;
import ru.albina.planner.dto.planner.DayDto;
import ru.albina.planner.dto.planner.DoctorScheduleDto;
import ru.albina.planner.dto.planner.ScheduleDto;
import ru.albina.planner.dto.planner.TaskDto;
import ru.albina.planner.mapper.ModalityMapper;
import ru.albina.planner.service.schedule.MonthWorkScheduleService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerScheduleService {

    private final MonthWorkScheduleService monthWorkScheduleService;

    @Transactional
    public List<ScheduleDto> generate(LocalDate startDate) {
        final var result = new LinkedList<ScheduleDto>();

        final var schedule = this.monthWorkScheduleService.getAllAtMonth(startDate)
                .stream()
                .collect(Collectors.groupingBy(WorkScheduleEntity::getWeekNumber, Collectors.toList()));
        for (final var weekToDay : schedule.entrySet()) {
            final var days = weekToDay.getValue().stream()
                    .collect(Collectors.toMap(WorkScheduleEntity::getDate, WorkScheduleEntity::getDoctorSchedules));

            result.add(ScheduleDto.builder()
                    .weekNumber(weekToDay.getKey())
                    .doctors(this.convertMap(days))
                    .build()
            );
        }
        return result;
    }


    private List<DoctorScheduleDto> convertMap(Map<LocalDate, List<DoctorScheduleEntity>> dateToDoctors) {
        final var now = LocalDate.now();

        Map<UUID, List<DayDto>> doctorToDates = new HashMap<>();

        for (final var entry : dateToDoctors.entrySet()) {
            final var date = entry.getKey();
            final var doctors = entry.getValue();

            for (final var doctor : doctors) {
                final var id = doctor.getDoctorId();
                if (!doctorToDates.containsKey(id)) {
                    doctorToDates.put(id, new ArrayList<>());
                }
                doctorToDates.get(id).add(
                        DayDto.builder()
                                .date(date)
                                .extraHours(doctor.getManualExtraHours())
                                .forceSchedule(doctor.getForceSchedule())
                                .tasks(
                                        date.isBefore(now) ?
                                                doctor.getDoctorWorks().stream().map(
                                                        work ->
                                                                TaskDto.builder()
                                                                        .extraHours(work.getUsedExtraHours())
                                                                        .hours(work.getUsedHours())
                                                                        .modality(ModalityMapper.to(work.getModality(), work.getTypeModality()))
                                                                        .build()
                                                ).toList()
                                                : Collections.emptyList()
                                )
                                .build()
                );
            }
        }

        return doctorToDates.entrySet().stream().map(
                entry -> DoctorScheduleDto.builder()
                        .id(entry.getKey())
                        .days(entry.getValue())
                        .build()
        ).toList();
    }
}
