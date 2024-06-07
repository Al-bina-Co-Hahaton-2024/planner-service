package ru.albina.planner.mapper;

import org.mapstruct.Mapper;
import ru.albina.planner.configuration.MapperConfiguration;
import ru.albina.planner.dto.medical.AbsenceSchedule;
import ru.albina.planner.dto.medical.Doctor;
import ru.albina.planner.dto.medical.Performance;
import ru.albina.planner.dto.planner.DoctorDto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(config = MapperConfiguration.class, uses = JavaTimeMapper.class)
public interface MedicalMapper {

    DoctorDto from(Doctor doctor);


    default List<LocalDate> flat(List<AbsenceSchedule> absenceSchedules) {
        return absenceSchedules.stream()
                .map(AbsenceSchedule::toFlatList)
                .flatMap(Collection::stream)
                .toList();
    }

    default Map<String, Integer> performances(List<Performance> performances) {
        return performances.stream()
                .map(performance -> Map.entry(performance.getModality() + "_" + performance.getTypeModality(), performance.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
