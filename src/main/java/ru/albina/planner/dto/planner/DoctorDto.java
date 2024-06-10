package ru.albina.planner.dto.planner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {

    private UUID id;
    private Double hours;
    private Double rate;

    private LocalDate startContract;
    private LocalDate endContract;

    private Set<String> modality;
    private Set<String> optionalModality;
    private List<Integer> workDays;
    private List<LocalDate> absenceSchedules;
    private Map<String, Integer> performances;
}
