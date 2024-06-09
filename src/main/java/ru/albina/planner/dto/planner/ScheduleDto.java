package ru.albina.planner.dto.planner;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScheduleDto {
    private int weekNumber;
    private List<DoctorScheduleDto> doctors;
}
