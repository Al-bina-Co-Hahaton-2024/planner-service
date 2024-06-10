package ru.albina.planner.dto.response.schedule;

import lombok.Builder;
import lombok.Data;
import ru.albina.planner.dto.TypeModality;
import ru.albina.planner.dto.medical.Modality;

@Data
@Builder
public class DoctorWork {

    private Modality modality;

    private TypeModality typeModality;

    private double usedHours;

    private double usedExtraHours;
}
