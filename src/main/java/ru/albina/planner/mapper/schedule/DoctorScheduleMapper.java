package ru.albina.planner.mapper.schedule;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.albina.planner.configuration.MapperConfiguration;
import ru.albina.planner.domain.DoctorScheduleEntity;
import ru.albina.planner.domain.DoctorWorkEntity;
import ru.albina.planner.dto.response.schedule.DoctorLoad;

@Mapper(config = MapperConfiguration.class)
public interface DoctorScheduleMapper {


    @Mapping(target = "takenHours", ignore = true)
    @Mapping(target = "takenExtraHours", ignore = true)
    DoctorLoad to(DoctorScheduleEntity entity);


    @AfterMapping
    default void after(@MappingTarget DoctorLoad.DoctorLoadBuilder doctorLoad, DoctorScheduleEntity entity) {
        var usedHours = 0.d;
        var usedExtraHours = 0.d;

        for (DoctorWorkEntity doctorWork : entity.getDoctorWorks()) {
            usedHours += doctorWork.getUsedHours();
            usedExtraHours += doctorWork.getUsedExtraHours();
        }

        doctorLoad.takenHours(usedHours);
        doctorLoad.takenExtraHours(usedExtraHours);
    }


}
