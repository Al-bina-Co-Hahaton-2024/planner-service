package ru.albina.planner.service.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.albina.planner.dto.request.GetWorkSchedulesRequest;
import ru.albina.planner.dto.response.schedule.DayWorkSchedule;
import ru.albina.planner.mapper.schedule.DayWorkScheduleMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkScheduleUserService {

    private final MonthWorkScheduleService monthWorkScheduleService;
    private final DayWorkScheduleMapper dayWorkScheduleMapper;

    @Transactional
    public List<DayWorkSchedule> getDayWorkSchedules(LocalDate scheduleDate) {
        return this.monthWorkScheduleService.getAllAtMonth(scheduleDate).stream()
                .map(this.dayWorkScheduleMapper::to)
                .toList();
    }

    @Transactional
    public List<DayWorkSchedule> getDayWorkSchedulesWithProduction(LocalDate scheduleDate) {
        return this.monthWorkScheduleService.getAllAtMonthWithProductionWeek(scheduleDate).stream()
                .map(this.dayWorkScheduleMapper::to)
                .toList();
    }

    @Transactional
    public List<DayWorkSchedule> getDayWorkSchedulesForDoctor(LocalDate scheduleDate, UUID doctorId) {
        return this.getDayWorkSchedules(scheduleDate).stream()
                .map(val ->
                        val.setDoctorSchedules(
                                val.getDoctorSchedules().stream()
                                        .filter(doctorLoad -> doctorLoad.getDoctorId().equals(doctorId))
                                        .toList()
                        )
                )
                .toList();
    }

    @Transactional
    public List<DayWorkSchedule> getDayWorkSchedulesWithProductionWithFilter(GetWorkSchedulesRequest request) {
        final var result = this.getDayWorkSchedulesWithProduction(request.getDate());

        if (!Optional.ofNullable(request.getDoctors()).orElse(Set.of()).isEmpty()) {
            result.forEach(day -> day.setDoctorSchedules(
                    day.getDoctorSchedules().stream()
                            .filter(doctorLoad -> request.getDoctors().contains(doctorLoad.getDoctorId()))
                            .toList()
            ));
        }
        return result;
    }
}
