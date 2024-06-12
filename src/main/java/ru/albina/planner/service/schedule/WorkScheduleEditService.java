package ru.albina.planner.service.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.albina.planner.domain.WorkScheduleEntity;
import ru.albina.planner.exception.EntityNotFoundException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkScheduleEditService {

    private final WorkScheduleService workScheduleService;

    @Transactional
    public void extraHours(UUID workSchedulerId, UUID doctorSchedulerId, Double extraHours) {
        final var doctorScheduler = this.getForEdit(workSchedulerId)
                .getDoctorSchedules().stream()
                .filter(schedule -> schedule.getId().equals(doctorSchedulerId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Can't find doctor schedule by id: " + doctorSchedulerId));
        doctorScheduler.setManualExtraHours(Math.max(0, extraHours));
    }

    @Transactional
    public WorkScheduleEntity getForEdit(UUID workSchedulerId) {
        return this.workScheduleService.getById(workSchedulerId)
                .setIsActual(false);
    }
}
