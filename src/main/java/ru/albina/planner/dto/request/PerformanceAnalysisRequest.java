package ru.albina.planner.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.albina.planner.dto.TypeModality;
import ru.albina.planner.dto.medical.Modality;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceAnalysisRequest {

    @NotNull
    private UUID doctorId;

    @NotNull
    private Modality modality;

    @NotNull
    private TypeModality typeModality;

    @NotNull
    private Double hours;
}
