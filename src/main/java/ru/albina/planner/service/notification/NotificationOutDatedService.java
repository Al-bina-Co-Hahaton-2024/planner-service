package ru.albina.planner.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.albina.planner.dto.request.PlannerOutDateDaysNotification;
import ru.albina.planner.service.calendar.CalendarService;
import ru.albina.planner.service.schedule.WorkScheduleService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationOutDatedService {

    private final WorkScheduleService workScheduleService;

    private final CalendarService calendarService;

    @Transactional
    public void notifyOutDates(PlannerOutDateDaysNotification planner) {

        this.calendarService.getAllDaysAtStartToEnd(planner.getStart(), planner.getEnd()).stream()
                .map(this.workScheduleService::findByDate)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(workScheduleEntity -> workScheduleEntity.setIsActual(false));

    }
}
