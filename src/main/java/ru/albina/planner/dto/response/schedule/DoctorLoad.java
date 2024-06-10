package ru.albina.planner.dto.response.schedule;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class DoctorLoad {

    private UUID doctorId;

    private Double manualExtraHours;

    private boolean forceSchedule;

    private Double takenHours;

    private Double takenExtraHours;

    private List<DoctorWork> doctorWorks;

}
