package ru.albina.planner.dto.planner;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlannerDto {
    private double monthlyHours;
    private List<DoctorDto> doctors;
    private List<WorkloadDto> workload;
    private List<ScheduleDto> schedule;
}
