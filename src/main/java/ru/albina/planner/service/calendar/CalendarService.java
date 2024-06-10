package ru.albina.planner.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CalendarService {

    public Set<LocalDate> getAllDaysAtMonth(LocalDate localDate) {
        var current = localDate.withDayOfMonth(1);
        final var result = new HashSet<LocalDate>();
        do {
            result.add(current);
            current = current.plusDays(1);
        } while (current.getMonthValue() == localDate.getMonthValue());
        return result;
    }
}
