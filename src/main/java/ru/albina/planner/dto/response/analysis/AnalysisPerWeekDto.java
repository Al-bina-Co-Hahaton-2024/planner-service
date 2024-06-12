package ru.albina.planner.dto.response.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisPerWeekDto {

    private List<AnalysisWorkload> workloads;

    private boolean isActual;


    private int year;

    private int weekNumber;
}
