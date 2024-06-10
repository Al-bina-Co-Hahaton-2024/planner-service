package ru.albina.planner.dto.planner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayDto {

    private LocalDate date;
    private Double extraHours;
    private boolean forceSchedule;
    private List<TaskDto> tasks;
}
