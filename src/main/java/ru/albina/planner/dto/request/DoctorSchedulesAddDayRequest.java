package ru.albina.planner.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSchedulesAddDayRequest {

    private LocalDate date;

    private UUID doctorId;

    private Double extraHours;
}
