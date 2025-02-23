package ru.albina.planner.mapper.schedule;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.albina.planner.configuration.MapperConfiguration;
import ru.albina.planner.domain.DoctorScheduleEntity;
import ru.albina.planner.domain.DoctorWorkEntity;
import ru.albina.planner.dto.response.schedule.DoctorLoad;

import java.util.Optional;

@Mapper(config = MapperConfiguration.class, uses = DoctorWorkMapper.class)
public interface DoctorScheduleMapper {


    @Mapping(target = "takenHours", ignore = true)
    @Mapping(target = "takenExtraHours", ignore = true)
    DoctorLoad to(DoctorScheduleEntity entity);


    @AfterMapping
    default void after(@MappingTarget DoctorLoad.DoctorLoadBuilder doctorLoad, DoctorScheduleEntity entity) {
        var usedHours = 0.d;
        var usedExtraHours = 0.d;

        for (DoctorWorkEntity doctorWork : entity.getDoctorWorks()) {
            usedHours += Optional.ofNullable(doctorWork.getUsedHours()).orElse(0.d);
            usedExtraHours += Optional.ofNullable(doctorWork.getUsedExtraHours()).orElse(0.d);
        }

        doctorLoad.takenHours(usedHours);
        doctorLoad.takenExtraHours(usedExtraHours);
    }


}
