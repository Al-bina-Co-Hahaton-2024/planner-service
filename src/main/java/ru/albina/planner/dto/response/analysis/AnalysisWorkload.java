package ru.albina.planner.dto.response.analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.albina.planner.dto.TypeModality;
import ru.albina.planner.dto.medical.Modality;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisWorkload {

    private Modality modality;

    private TypeModality typeModality;

    private Long workload;

    private Long work;

    private Double hoursNeed;
}
