package ru.albina.planner.dto.planner;

import lombok.Builder;

import java.util.List;

@Builder
public class DayDto {

    private String date;
    private int extraHours;
    private List<TaskDto> tasks;
}
