package ru.albina.planner.dto.planner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkloadDto {
    private int week;
    private String modality;
    private Long value;

}
