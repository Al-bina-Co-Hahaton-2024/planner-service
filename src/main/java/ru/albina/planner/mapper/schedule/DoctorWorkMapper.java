package ru.albina.planner.mapper.schedule;

import org.mapstruct.Mapper;
import ru.albina.planner.configuration.MapperConfiguration;
import ru.albina.planner.domain.DoctorWorkEntity;
import ru.albina.planner.dto.response.schedule.DoctorWork;

@Mapper(config = MapperConfiguration.class)
public interface DoctorWorkMapper {

    DoctorWork from(DoctorWorkEntity doctorWork);
}
