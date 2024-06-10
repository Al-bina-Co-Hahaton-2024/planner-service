package ru.albina.planner.service.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.albina.planner.client.ReferenceClient;
import ru.albina.planner.domain.WorkScheduleEntity;
import ru.albina.planner.repository.WorkScheduleRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Transactional
    public List<WorkScheduleEntity> createOrGet(Collection<LocalDate> dates) {
        final var entities = this.workScheduleRepository.findAllByDateIn(dates);


        final var found = entities.stream().map(WorkScheduleEntity::getDate).collect(Collectors.toSet());

        final var saved = this.workScheduleRepository.saveAll(
                dates.stream().filter(date -> !found.contains(date))
                        .map(date ->
                                new WorkScheduleEntity()
                                        .setId(UUID.randomUUID())
                                        .setDate(date)
                                        .setWeekNumber(this.referenceClient.getWeek(date).getWeekNumber())
                                        .setIsActual(false)
                        )
                        .toList()
        );


        return Stream.concat(
                entities.stream(),
                saved.stream()
        ).toList();
    }

    @Transactional(readOnly = true)
    public Optional<WorkScheduleEntity> findByDate(LocalDate date) {
        return this.workScheduleRepository.findByDate(date);
    }

    @Transactional
    public void save(WorkScheduleEntity day) {
        this.workScheduleRepository.save(day);
    }
}
