package ru.albina.planner.dto.medical;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class AbsenceSchedule {

    private LocalDate start;

    private LocalDate end;


    public List<LocalDate> toFlatList() {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate current = start;
        while (!current.isAfter(end)) {
            dateList.add(current);
            current = current.plusDays(1);
        }
        return dateList;
    }
}
