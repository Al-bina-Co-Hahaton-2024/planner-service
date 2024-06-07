package ru.albina.planner.dto.planner;

import lombok.Data;

@Data
public class WorkloadDto {
    private int week;
    private String modality;
    private Long value;

}
