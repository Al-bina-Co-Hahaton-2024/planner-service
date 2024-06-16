package ru.albina.planner.service.planner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.albina.planner.client.ReferenceClient;
import ru.albina.planner.dto.reference.WeekNumberResult;
import ru.albina.planner.service.calendar.CalendarService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerWeekNumberService {

    private final ReferenceClient referenceClient;
    private final CalendarService calendarService;

    public List<WeekNumberResult> generate(LocalDate startDate) {
        return this.referenceClient.getWeeks(this.calendarService.getAllDaysAtMonth(startDate));
    }
}
