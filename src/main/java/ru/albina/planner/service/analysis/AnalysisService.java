package ru.albina.planner.service.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.albina.planner.client.MedicalClient;
import ru.albina.planner.client.ReferenceClient;
import ru.albina.planner.dto.medical.Doctor;
import ru.albina.planner.dto.medical.Performance;
import ru.albina.planner.dto.reference.GetOrGenerateYearlyWorkloadRequest;
import ru.albina.planner.dto.request.AnalysisPerWeekRequest;
import ru.albina.planner.dto.response.analysis.AnalysisPerWeekDto;
import ru.albina.planner.dto.response.analysis.AnalysisWorkload;
import ru.albina.planner.mapper.ModalityMapper;
import ru.albina.planner.service.calendar.CalendarService;
import ru.albina.planner.service.planner.PerformanceService;
import ru.albina.planner.service.schedule.WorkScheduleService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final MedicalClient medicalClient;

    private final ReferenceClient referenceClient;

    private final CalendarService calendarService;

    private final WorkScheduleService workScheduleService;

    private final PerformanceService performanceService;

    private AnalysisPerWeekDto getWeekAnalysis(Map<UUID, Doctor> doctors, int year, int week) {
        final var weekNumberResult = this.referenceClient.getWeek(year, week);
        final var averagePerformance = this.medicalClient.getAveragePerformances();
        final var schedules = this.workScheduleService.createOrGet(
                this.calendarService.getAllDaysAtStartToEnd(weekNumberResult.getStartDate(), weekNumberResult.getEndDate())
        );
        final var modalityResults = new HashMap<String, Long>();

        boolean isActual = true;

        for (final var schedule : schedules) {
            isActual = isActual && schedule.getIsActual();
            schedule.getDoctorSchedules().forEach(doctorSchedule -> {
                doctorSchedule.getDoctorWorks().forEach(work -> {
                    final var doctorPerformance = Optional.ofNullable(doctors.get(doctorSchedule.getDoctorId()))
                            .map(Doctor::getPerformances)
                            .orElse(List.of()).stream()
                            .filter(performance -> performance.getModality() == work.getModality() && performance.getTypeModality() == work.getTypeModality())
                            .map(Performance::getValue)
                            .findFirst()
                            .orElse(0);

                    final var key = ModalityMapper.to(work.getModality(), work.getTypeModality());
                    final var hours = work.getUsedHours() + work.getUsedExtraHours();
                    final long score = this.performanceService.calculatePerformance(doctorPerformance, hours);

                    if (modalityResults.containsKey(key)) {
                        modalityResults.put(key, modalityResults.get(key) + score);
                    } else {
                        modalityResults.put(key, score);
                    }
                });
            });
        }

        final var analyzes = this.referenceClient.getWorkload(
                List.of(
                        GetOrGenerateYearlyWorkloadRequest.builder()
                                .year(year)
                                .weeks(Set.of(week))
                                .build()
                )
        ).stream().map(workload -> {
                    final var workloadValue = Optional.ofNullable(workload.getManualValue()).orElse(workload.getGeneratedValue());
                    final var work = modalityResults.getOrDefault(ModalityMapper.to(workload.getModality(), workload.getTypeModality()), 0L);

                    final var average = averagePerformance.stream()
                            .filter(performance ->
                                    performance.getTypeModality().equals(workload.getTypeModality()) && performance.getModality().equals(workload.getModality())
                            )
                            .map(Performance::getValue)
                            .findFirst().orElse(0);

                    return AnalysisWorkload.builder()
                            .modality(workload.getModality())
                            .typeModality(workload.getTypeModality())
                            .workload(workloadValue)
                            .work(work)
                            .hoursNeed(this.performanceService.delegatePerformance(workloadValue - work, average, Double.MAX_VALUE))
                            .build();
                }
        ).toList();


        return AnalysisPerWeekDto.builder()
                .isActual(isActual)
                .weekNumber(week)
                .year(year)
                .workloads(analyzes)
                .build();
    }


    @Transactional(readOnly = true)
    public List<AnalysisPerWeekDto> getWeekAnalysis(List<AnalysisPerWeekRequest> requests) {

        final var doctors = this.medicalClient.getDoctors().stream().collect(Collectors.toMap(Doctor::getId, Function.identity()));

        return requests.stream().map(
                request -> this.getWeekAnalysis(doctors, request.getYear(), request.getWeek())
        ).toList();
    }


    @Transactional(readOnly = true)
    public AnalysisPerWeekDto getWeekAnalysis(int year, int week) {
        return this.getWeekAnalysis(List.of(AnalysisPerWeekRequest.builder().year(year).week(week).build())).get(0);
    }

}
