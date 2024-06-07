package ru.albina.planner.dto.planner;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DoctorScheduleDto {
    private UUID id;
    private List<DayDto> days;
}
