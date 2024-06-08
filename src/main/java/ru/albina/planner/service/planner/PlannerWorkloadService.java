package ru.albina.planner.service.planner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.albina.planner.client.ReferenceClient;
import ru.albina.planner.dto.planner.WorkloadDto;
import ru.albina.planner.dto.reference.GetOrGenerateYearlyWorkloadRequest;
import ru.albina.planner.mapper.ReferenceMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PlannerWorkloadService {

    private final ReferenceClient referenceClient;

    private final ReferenceMapper referenceMapper;

    public List<WorkloadDto> generate(LocalDate startDate) {

        final var endDate = startDate.withDayOfMonth(startDate.getMonth().length(startDate.isLeapYear()));

        final var weekNumberResult = this.referenceClient.getWeek(startDate);
        final var endWeekNumberResult = this.referenceClient.getWeek(endDate);

        final var request = new ArrayList<GetOrGenerateYearlyWorkloadRequest>();

        if (weekNumberResult.getStartDate().getYear() != startDate.getYear()) {
            request.add(
                    GetOrGenerateYearlyWorkloadRequest.builder()
                            .year(startDate.getYear() - 1)
                            .weeks(Set.of(weekNumberResult.getWeekNumber()))
                            .build()
            );
            request.add(
                    GetOrGenerateYearlyWorkloadRequest.builder()
                            .year(startDate.getYear())
                            .weeks(IntStream.range(1, endWeekNumberResult.getWeekNumber() + 1).boxed().collect(Collectors.toSet()))
                            .build()
            );
        } else {
            request.add(
                    GetOrGenerateYearlyWorkloadRequest.builder()
                            .year(startDate.getYear())
                            .weeks(IntStream.range(weekNumberResult.getWeekNumber(), endWeekNumberResult.getWeekNumber() + 1).boxed().collect(Collectors.toSet()))
                            .build()
            );
        }


        return this.referenceClient.getWorkload(request).stream()
                .map(this.referenceMapper::to)
                .toList();
    }
}
