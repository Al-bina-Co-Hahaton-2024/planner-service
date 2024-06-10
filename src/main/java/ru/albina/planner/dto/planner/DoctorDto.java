package ru.albina.planner.dto.planner;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
public class DoctorDto {

    private UUID id;
    private Double hours;
    private Double rate;
    private Set<String> modality;
    private Set<String> optionalModality;
    private List<Integer> workDays;
    private List<LocalDate> absenceSchedules;
    private Map<String, Integer> performances;
}
