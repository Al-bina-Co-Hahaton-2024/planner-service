package ru.albina.planner.service.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.albina.planner.client.MedicalClient;
import ru.albina.planner.domain.DoctorScheduleEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkScheduleExtraHoursService {

    private final WorkScheduleService workScheduleService;

    private final MedicalClient medicalClient;

    @Transactional
    public void addExtraHours(LocalDate date, UUID doctorId, Double hours) {
        //For check.
        this.medicalClient.getDoctor(doctorId);

        final var workSchedule = this.workScheduleService.createOrGet(date).setIsActual(false);

        workSchedule.getDoctorSchedules().add(
                new DoctorScheduleEntity()
                        .setId(UUID.randomUUID())
                        .setDoctorId(doctorId)
                        .setDoctorWorks(List.of())
                        .setManualExtraHours(hours)
                        .setWorkSchedule(workSchedule)
        );
    }
}
