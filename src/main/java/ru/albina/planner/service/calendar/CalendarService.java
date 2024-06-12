package ru.albina.planner.service.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.albina.planner.client.ReferenceClient;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final ReferenceClient referenceClient;

    public Set<LocalDate> getAllDaysAtMonth(LocalDate localDate) {
        var current = localDate.withDayOfMonth(1);
        final var result = new HashSet<LocalDate>();
        do {
            result.add(current);
            current = current.plusDays(1);
        } while (current.getMonthValue() == localDate.getMonthValue());
        return result;
    }


    public Set<LocalDate> getAllDaysAtStartToEnd(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        final var result = new HashSet<LocalDate>();
        do {
            result.add(startDate);
            startDate = startDate.plusDays(1);
        } while (
                startDate.isBefore(endDate) || startDate.isEqual(endDate)
        );
        return result;
    }


    public LocalDate getLastDayOfMonth(LocalDate localDate) {
        return localDate.withDayOfMonth(localDate.getMonth().length(localDate.isLeapYear()));
    }


    public Set<LocalDate> getAllDaysForMonthWithProductionWeek(LocalDate localDate) {

        return this.getAllDaysAtStartToEnd(
                this.referenceClient.getWeek(localDate.withDayOfMonth(1)).getStartDate(),
                this.referenceClient.getWeek(this.getLastDayOfMonth(localDate)).getEndDate()
        );
    }
}
