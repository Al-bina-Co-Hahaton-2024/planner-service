package ru.albina.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.albina.planner.domain.WorkScheduleEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkScheduleEntity, UUID> {

    Optional<WorkScheduleEntity> findByDate(LocalDate date);
}
