package ru.albina.planner.dto.medical;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.albina.planner.dto.TypeModality;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Performance {

    private Modality modality;

    private TypeModality typeModality;

    private int value;
}
