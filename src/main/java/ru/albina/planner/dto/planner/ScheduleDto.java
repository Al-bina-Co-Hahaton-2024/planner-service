package ru.albina.planner.dto.planner;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleDto {
    private int weekNumber;
    private List<DoctorScheduleDto> doctors;
}
