package ru.albina.planner.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkSchedulesRequest {

    private LocalDate workScheduleDate;
}
