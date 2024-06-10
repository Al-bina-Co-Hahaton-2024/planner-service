package ru.albina.planner.dto.response.data;

import lombok.Builder;
import ru.albina.planner.dto.TypeModality;
import ru.albina.planner.dto.medical.Modality;

@Builder
public class TaskDto {

    private Modality modality;

    private TypeModality typeModality;

    private double hours;

    private double extraHours;
}
