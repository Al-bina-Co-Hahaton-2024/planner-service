package ru.albina.planner.dto.planner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.albina.planner.dto.reference.WeekNumberResult;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannerDto {
    private LocalDate  month;
    private double monthlyHours;
    private List<DoctorDto> doctors;
    private List<WorkloadDto> workload;
    private List<WeekNumberResult> weekNumbers;
    private List<ScheduleDto> schedule;
}
