package ru.albina.planner.service.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.albina.planner.client.ReferenceClient;
import ru.albina.planner.domain.WorkScheduleEntity;
import ru.albina.planner.repository.WorkScheduleRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {

    private final ReferenceClient referenceClient;

    private final WorkScheduleRepository workScheduleRepository;

    @Transactional
    public WorkScheduleEntity createOrGet(LocalDate date) {
        return this.findByDate(date).orElseGet(
                () -> this.workScheduleRepository.save(
                        new WorkScheduleEntity()
                                .setId(UUID.randomUUID())
                                .setDate(date)
                                .setWeekNumber(this.referenceClient.getWeek(date).getWeekNumber())
                                .setIsActual(false)
                )
        );
    }

    @Transactional(readOnly = true)
    public Optional<WorkScheduleEntity> findByDate(LocalDate date) {
        return this.workScheduleRepository.findByDate(date);
    }
}
