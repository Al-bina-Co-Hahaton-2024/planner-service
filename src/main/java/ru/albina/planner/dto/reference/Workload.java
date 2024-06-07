package ru.albina.planner.dto.reference;

import lombok.Data;
import ru.albina.planner.dto.TypeModality;
import ru.albina.planner.dto.medical.Modality;


@Data
public class Workload {

    private int year;

    private int week;

    private Modality modality;

    private TypeModality typeModality;

    private Long manualValue;

    private Long generatedValue;
}
