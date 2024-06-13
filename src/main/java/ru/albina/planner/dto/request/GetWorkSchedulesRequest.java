package ru.albina.planner.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
public class GetWorkSchedulesRequest {

    private LocalDate date;

    private Set<UUID> doctors;
}
