package ru.albina.planner.dto.planner;

import lombok.Builder;

@Builder
public class TaskDto {

    private String modality;
    private int hours;
    private int extraHours;
}
