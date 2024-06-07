package ru.albina.planner.dto.reference;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class GetOrGenerateYearlyWorkloadRequest {

    private int year;

    private Set<Integer> weeks;
}
