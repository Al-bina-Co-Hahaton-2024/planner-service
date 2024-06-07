package ru.albina.planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.albina.planner.configuration.MapperConfiguration;
import ru.albina.planner.dto.planner.WorkloadDto;
import ru.albina.planner.dto.reference.Workload;

import java.util.Optional;

@Mapper(config = MapperConfiguration.class)
public interface ReferenceMapper {

    @Mapping(target = "modality", source = ".")
    @Mapping(target = "value", source = ".")
    WorkloadDto to(Workload workload);


    default String modality(Workload workload) {
        return ModalityMapper.to(workload.getModality(), workload.getTypeModality());
    }

    default Long value(Workload workload) {
        return Optional.ofNullable(workload.getManualValue()).orElse(workload.getGeneratedValue());
    }
}
