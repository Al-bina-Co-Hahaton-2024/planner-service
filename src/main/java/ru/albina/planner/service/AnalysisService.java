package ru.albina.planner.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.albina.planner.client.MedicalClient;
import ru.albina.planner.client.ReferenceClient;
import ru.albina.planner.dto.medical.Doctor;
import ru.albina.planner.dto.medical.Performance;
import ru.albina.planner.dto.reference.GetOrGenerateYearlyWorkloadRequest;
import ru.albina.planner.dto.response.analysis.AnalysisPerWeekDto;
import ru.albina.planner.dto.response.analysis.AnalysisWorkload;
import ru.albina.planner.mapper.ModalityMapper;
import ru.albina.planner.service.calendar.CalendarService;
import ru.albina.planner.service.schedule.WorkScheduleService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @Transactional(readOnly = true)
    public AnalysisPerWeekDto getWeekAnalysis(int year, int week) {
        final var weekNumberResult = this.referenceClient.getWeek(year, week);
        final var schedules = this.workScheduleService.createOrGet(
                this.calendarService.getAllDaysAtStartToEnd(weekNumberResult.getStartDate(), weekNumberResult.getEndDate())
        );
        final var modalityResults = new HashMap<String, Long>();

        final var doctors = this.medicalClient.getDoctors().stream().collect(Collectors.toMap(Doctor::getId, Function.identity()));

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


                    log.info("{} - {}", doctorSchedule.getDoctorId(), doctorPerformance);

                    final var key = ModalityMapper.to(work.getModality(), work.getTypeModality());
                    final var hours = work.getUsedHours() + work.getUsedExtraHours();
                    final long score = (long) (doctorPerformance * hours);


                    log.info("{} ", score);

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
        ).stream().map(workload ->
                AnalysisWorkload.builder()
                        .modality(workload.getModality())
                        .typeModality(workload.getTypeModality())
                        .workload(Optional.ofNullable(workload.getManualValue()).orElse(workload.getGeneratedValue()))
                        .work(modalityResults.get(ModalityMapper.to(workload.getModality(), workload.getTypeModality())))
                        .build()
        ).toList();


        return AnalysisPerWeekDto.builder()
                .isActual(isActual)
                .workloads(analyzes)
                .build();
    }
}
