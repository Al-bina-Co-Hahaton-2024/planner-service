package ru.albina.planner.mapper.schedule;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.albina.planner.configuration.MapperConfiguration;
import ru.albina.planner.domain.WorkScheduleEntity;
import ru.albina.planner.dto.response.schedule.DayWorkSchedule;

@Mapper(config = MapperConfiguration.class, uses = DoctorScheduleMapper.class)
public interface DayWorkScheduleMapper {

    @Mapping(target = "doctors", source = "doctorSchedules")
    DayWorkSchedule to(WorkScheduleEntity workScheduleEntity);
}
