package ru.albina.planner.mapper;

import lombok.Builder;
import ru.albina.planner.dto.TypeModality;
import ru.albina.planner.dto.medical.Modality;

public interface ModalityMapper {


    static String to(Modality modality, TypeModality typeModality) {
        if (typeModality == null || typeModality == TypeModality.DEFAULT) {
            return modality.toString();
        }
        return modality + "_" + typeModality;
    }

    static ModalityContainer from(String modality) {
        final var arr = modality.split("_");
        return ModalityContainer.builder()
                .modality(Modality.valueOf(arr[0]))
                .typeModality(arr.length == 2 ? TypeModality.valueOf(arr[1]) : TypeModality.DEFAULT)
                .build();
    }

    @Builder
    static record ModalityContainer(Modality modality, TypeModality typeModality) {
    }
}
