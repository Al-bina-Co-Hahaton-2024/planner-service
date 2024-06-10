package ru.albina.planner.dto.response.data;

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
public class TaskDayDto {

    private Modality modality;

    private TypeModality typeModality;

    private double hours;

    private double extraHours;
}
