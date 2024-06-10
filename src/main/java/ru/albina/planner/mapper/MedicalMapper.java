package ru.albina.planner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.albina.planner.configuration.MapperConfiguration;
import ru.albina.planner.dto.TypeModality;
import ru.albina.planner.dto.medical.AbsenceSchedule;
import ru.albina.planner.dto.medical.Doctor;
import ru.albina.planner.dto.medical.Modality;
import ru.albina.planner.dto.medical.Performance;
import ru.albina.planner.dto.planner.DoctorDto;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Mapper(config = MapperConfiguration.class, uses = JavaTimeMapper.class)
public interface MedicalMapper {

    DoctorDto from(Doctor doctor);


    default Set<String> modality(Modality modality) {
        if (modality == null){
            return Collections.emptySet();
        }
        final var hashSet = new HashSet<String>();
        hashSet.add(ModalityMapper.to(modality, TypeModality.DEFAULT));
        if (modality == Modality.KT || modality == Modality.MRT) {
            hashSet.add(ModalityMapper.to(modality, TypeModality.U));
            hashSet.add(ModalityMapper.to(modality, TypeModality.U2));
        }
        return hashSet;
    }


    default Set<String> modality(Set<Modality> modality) {
        return modality.stream().map(this::modality)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    default List<LocalDate> flat(List<AbsenceSchedule> absenceSchedules) {
        return absenceSchedules.stream()
                .map(AbsenceSchedule::toFlatList)
                .flatMap(Collection::stream)
                .toList();
    }

    default Map<String, Integer> performances(List<Performance> performances) {
        return performances.stream()
                .map(performance -> Map.entry(ModalityMapper.to(performance.getModality(), performance.getTypeModality()), performance.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
