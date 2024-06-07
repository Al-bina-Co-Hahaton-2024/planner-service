package ru.albina.planner.mapper;

import ru.albina.planner.dto.TypeModality;
import ru.albina.planner.dto.medical.Modality;

public interface ModalityMapper {


    static String to(Modality modality, TypeModality typeModality) {
        if (typeModality == null || typeModality == TypeModality.DEFAULT) {
            return modality.toString();
        }
        return modality + "_" + typeModality;
    }
}
