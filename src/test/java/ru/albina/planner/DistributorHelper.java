package ru.albina.planner;

import ru.albina.planner.dto.medical.Modality;
import ru.albina.planner.dto.planner.DoctorDto;
import ru.albina.planner.dto.planner.WorkloadDto;
import ru.albina.planner.dto.reference.WeekNumberResult;

import java.time.LocalDate;
import java.util.*;

public class DistributorHelper {


    public static DoctorDto generateDoctor() {
        return DoctorDto.builder()
                .id(UUID.randomUUID())
                .hours(5d)
                .rate(1d)
                .modality(Set.of("DENSITOMETER"))
                .optionalModality(Set.of("FLG", "MRT"))
                .workDays(List.of(4, 7))
                .startContract(LocalDate.of(2023, 5, 1))
                .absenceSchedules(List.of())
                .performances(Map.of(
                        "FLG", 1,
                        "RG", 1,
                        "MRT_U", 1,
                        "MMG", 1,
                        "KT", 1,
                        "DENSITOMETER", 1,
                        "KT_U2", 1,
                        "KT_U", 1,
                        "MRT_U2", 1,
                        "MRT", 1))
                .build();
    }


    public static List<WeekNumberResult> weekNumberResults(LocalDate start, int count) {
        final var result = new ArrayList<WeekNumberResult>();
        var i = 1;
        while (count > 0) {
            result.add(
                    WeekNumberResult.builder()
                            .startDate(start)
                            .endDate(start.plusDays(6))
                            .weekNumber(i)
                            .build()
            );
            start = start.plusDays(7);
            count--;
            i++;
        }
        return result;
    }


    public static List<WorkloadDto> generateWorkload(int count) {
        final var result = new ArrayList<WorkloadDto>();

        for (int i = 1; i <= count; i++) {
            result.add(
                    WorkloadDto.builder()
                            .modality(Modality.MRT.toString())
                            .value(10L)
                            .week(i)
                            .build()
            );
        }

        return result;
    }
}
