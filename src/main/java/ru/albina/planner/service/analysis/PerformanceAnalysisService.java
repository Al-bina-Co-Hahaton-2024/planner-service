package ru.albina.planner.service.analysis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.albina.planner.client.MedicalClient;
import ru.albina.planner.dto.medical.Performance;
import ru.albina.planner.dto.request.PerformanceAnalysisRequest;
import ru.albina.planner.dto.response.performance.PerformanceAnalysis;
import ru.albina.planner.service.planner.PerformanceService;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PerformanceAnalysisService {

    private final MedicalClient medicalClient;

    private final PerformanceService performanceService;

    public PerformanceAnalysis perform(PerformanceAnalysisRequest request) {
        final var doctor = this.medicalClient.getDoctor(request.getDoctorId());

        final var doctorPerformance = doctor.getPerformances().stream()
                .filter(performance ->
                        performance.getModality() == request.getModality() && performance.getTypeModality() == request.getTypeModality()
                )
                .findFirst()
                .filter(performance ->
                        performance.getModality().equals(doctor.getModality()) ||
                                Optional.ofNullable(doctor.getOptionalModality()).orElse(Set.of()).contains(performance.getModality())
                )
                .map(Performance::getValue)
                .orElse(0);

        return PerformanceAnalysis.builder()
                .work(this.performanceService.calculatePerformance(doctorPerformance, request.getHours()))
                .build();
    }
}
