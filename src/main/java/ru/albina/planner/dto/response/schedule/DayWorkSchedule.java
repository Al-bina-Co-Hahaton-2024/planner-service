package ru.albina.planner.dto.response.schedule;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class DayWorkSchedule {

    private LocalDate date;

    private boolean isActual;

    private List<DoctorLoad> doctors;
}
