package ru.albina.planner.dto.response.schedule;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Accessors(chain = true)
public class DayWorkSchedule {

    private UUID id;

    private LocalDate date;

    private boolean isActual;

    private Integer weekNumber;

    private List<DoctorLoad> doctorSchedules;
}
