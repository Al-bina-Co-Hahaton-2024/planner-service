package ru.albina.planner.service.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.albina.planner.domain.WorkScheduleEntity;
import ru.albina.planner.service.calendar.CalendarService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthWorkScheduleService {

    private final CalendarService calendarService;

    private final WorkScheduleService workScheduleService;


    @Transactional
    public List<WorkScheduleEntity> getAllAtMonth(LocalDate localDate) {
        final var data = this.calendarService.getAllDaysAtMonth(localDate);

        return this.workScheduleService.createOrGet(data);
    }


    @Transactional
    public List<WorkScheduleEntity> getAllAtMonthWithProductionWeek(LocalDate localDate) {

        final var data = this.calendarService.getAllDaysForMonthWithProductionWeek(localDate);

        return this.workScheduleService.createOrGet(data);
    }
}
