package ru.albina.planner.service.planner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.albina.planner.client.ReferenceClient;
import ru.albina.planner.dto.reference.WeekNumberResult;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerWeekNumberService {

    private final ReferenceClient referenceClient;

    public List<WeekNumberResult> generate(LocalDate startDate) {
        final var result = new HashSet<LocalDate>();
        var mark = startDate;
        while (mark.getMonthValue() == startDate.getMonthValue()){
            result.add(mark);
            mark = mark.plusDays(7);
        }
        return this.referenceClient.getWeeks(result);
    }
}
