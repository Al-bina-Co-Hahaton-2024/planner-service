package ru.albina.planner.mapper;

import org.mapstruct.Mapper;
import ru.albina.planner.configuration.MapperConfiguration;

import java.time.DayOfWeek;

@Mapper(config = MapperConfiguration.class)
public interface JavaTimeMapper {


    default Integer dayOfWeek(DayOfWeek dayOfWeek) {
        return dayOfWeek.getValue();
    }
}
