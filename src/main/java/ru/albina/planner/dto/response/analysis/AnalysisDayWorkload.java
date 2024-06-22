package ru.albina.planner.dto.response.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisDayWorkload {

    private LocalDate date;

    private int doctors;
}
