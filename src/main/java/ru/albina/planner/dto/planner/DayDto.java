package ru.albina.planner.dto.planner;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class DayDto {

    private LocalDate date;
    private Double extraHours;
    private boolean forceSchedule;
    private List<TaskDto> tasks;
}
