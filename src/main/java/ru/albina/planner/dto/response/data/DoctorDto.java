package ru.albina.planner.dto.response.data;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DoctorDto {
    private UUID id;

    private List<TaskDto> tasks;
}
