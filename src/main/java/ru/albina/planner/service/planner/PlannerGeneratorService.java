package ru.albina.planner.service.planner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.albina.planner.client.ReferenceClient;
import ru.albina.planner.dto.planner.PlannerDto;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PlannerGeneratorService {

    private final ReferenceClient referenceClient;

    private final PlannerDoctorService plannerDoctorService;
    private final PlannerWorkloadService plannerWorkloadService;
    private final PlannerWeekNumberService weekNumberService;
    private final PlannerScheduleService plannerScheduleService;

    public PlannerDto generateRequest(LocalDate templateDate) {
        final var startDate = templateDate.withDayOfMonth(1);
        return PlannerDto.builder()
                .month(startDate)
                .monthlyHours(this.referenceClient.getHours(startDate.getYear(), startDate.getMonthValue()))
                .doctors(this.plannerDoctorService.generateDoctors())
                .workload(this.plannerWorkloadService.generate(startDate))
                .weekNumbers(this.weekNumberService.generate(startDate))
                .schedule(this.plannerScheduleService.generate(startDate))
                .build();
    }
}
