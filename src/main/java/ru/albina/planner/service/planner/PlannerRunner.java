package ru.albina.planner.service.planner;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.albina.planner.domain.DoctorScheduleEntity;
import ru.albina.planner.domain.DoctorWorkEntity;
import ru.albina.planner.domain.WorkScheduleEntity;
import ru.albina.planner.dto.response.data.DoctorDayDto;
import ru.albina.planner.service.planner.scheduler.DistributorService;
import ru.albina.planner.service.schedule.WorkScheduleService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlannerRunner {

    private static final Logger log = LoggerFactory.getLogger(PlannerRunner.class);
    private final PlannerGeneratorService plannerGeneratorService;
    private final DistributorService distributorService;

    private final WorkScheduleService workScheduleService;


    public void run(LocalDate date) {
        final var result = this.plannerGeneratorService.generateRequest(date);
        final var calendar = this.distributorService.distributeDoctors(result);

        calendar.entrySet().parallelStream().forEach(entry -> {
            final var localDate = entry.getKey();
            final var doctorDayDtos = entry.getValue();
            log.info("I am {}-{} for {}", Thread.currentThread().getName(), Thread.currentThread().getId(), localDate);
            final var day = this.workScheduleService.createOrGet(localDate);
            this.update(day, day.getDoctorSchedules(), doctorDayDtos);
            day.setIsActual(true);
            this.workScheduleService.save(day);
        });
    }

    private void update(WorkScheduleEntity day, List<DoctorScheduleEntity> doctors, List<DoctorDayDto> doctorDay) {
        final var existed = doctors.stream().collect(Collectors.toMap(DoctorScheduleEntity::getDoctorId, Function.identity()));
        final var toUpdate = doctorDay.stream().collect(Collectors.toMap(DoctorDayDto::getId, Function.identity()));


        doctors.removeAll(
                existed.keySet().stream()
                        .filter(v -> !toUpdate.containsKey(v))
                        .map(existed::get)
                        .toList()
        );

        for (final var updated : doctorDay) {
            final var currentDoctor = Optional.ofNullable(existed.get(updated.getId()))
                    .orElseGet(() -> {
                        final var doctor = new DoctorScheduleEntity()
                                .setId(UUID.randomUUID())
                                .setDoctorId(updated.getId())
                                .setManualExtraHours(0d)
                                .setWorkSchedule(day);
                        doctors.add(doctor);
                        return doctor;
                    });
            currentDoctor.getDoctorWorks().clear();
            currentDoctor.getDoctorWorks().addAll(
                    updated.getTasks().stream().map(taskDayDto ->
                            new DoctorWorkEntity()
                                    .setId(UUID.randomUUID())
                                    .setDoctorSchedule(currentDoctor)
                                    .setModality(taskDayDto.getModality())
                                    .setTypeModality(taskDayDto.getTypeModality())
                                    .setUsedHours(taskDayDto.getHours())
                                    .setUsedExtraHours(taskDayDto.getExtraHours())
                    ).toList()
            );
        }
    }
}
