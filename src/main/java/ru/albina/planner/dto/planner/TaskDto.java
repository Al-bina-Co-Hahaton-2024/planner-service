package ru.albina.planner.dto.planner;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDto {

    private String modality;
    private Double hours;
    private Double extraHours;
}
