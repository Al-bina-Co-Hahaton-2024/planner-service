package ru.albina.planner.dto.response.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDayDto {
    private UUID id;

    private Double extraHours;

    private List<TaskDayDto> tasks;
}
