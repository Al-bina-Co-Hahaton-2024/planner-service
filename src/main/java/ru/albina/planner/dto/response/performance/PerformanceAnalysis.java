package ru.albina.planner.dto.response.performance;

import jakarta.validation.constraints.NotNull;
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
public class PerformanceAnalysis {

    @NotNull
    private Double hours;

    @NotNull
    private Modality modality;

    @NotNull
    private TypeModality typeModality;

    private Long work;
}
