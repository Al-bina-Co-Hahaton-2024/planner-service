package ru.albina.planner.service.planner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.albina.planner.dto.planner.PlannerDto;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PlannerGeneratorService {

    private final PlannerDoctorService plannerDoctorService;
    private final PlannerWorkloadService plannerWorkloadService;

    public PlannerDto generateRequest(LocalDate templateDate) {
        final var startDate = templateDate.atStartOfDay().toLocalDate();
        return PlannerDto.builder()
                .doctors(this.plannerDoctorService.generateDoctors())
                .workload(this.plannerWorkloadService.generate(startDate))
                .build();
    }
}
