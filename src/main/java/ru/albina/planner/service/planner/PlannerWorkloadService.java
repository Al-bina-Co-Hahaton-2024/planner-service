package ru.albina.planner.service.planner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.albina.planner.client.ReferenceClient;
import ru.albina.planner.dto.planner.WorkloadDto;
import ru.albina.planner.dto.reference.GetOrGenerateYearlyWorkloadRequest;
import ru.albina.planner.mapper.ReferenceMapper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PlannerWorkloadService {
    private static final Locale RUSSIAN = new Locale("ru", "RUS");

    private static final WeekFields RUSSIAN_WEEK = WeekFields.of(RUSSIAN);


    private final ReferenceClient referenceClient;

    private final ReferenceMapper referenceMapper;

    public List<WorkloadDto> generate(LocalDate startDate) {

        final var endDate = startDate.withDayOfMonth(startDate.getMonth().length(startDate.isLeapYear()));

        final int startWeekNumber = startDate.get(RUSSIAN_WEEK.weekOfWeekBasedYear());

        final var request = new ArrayList<GetOrGenerateYearlyWorkloadRequest>();

        request.add(
                GetOrGenerateYearlyWorkloadRequest.builder()
                        .year(startDate.getYear())
                        .weeks(IntStream.range(startWeekNumber, endDate.get(RUSSIAN_WEEK.weekOfWeekBasedYear()) + 1).boxed().collect(Collectors.toSet()))
                        .build()
        );


        if (startWeekNumber == 1 && startDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            //contains prev year
            final var week = startDate.minusDays(7).get(RUSSIAN_WEEK.weekOfWeekBasedYear());
            request.add(
                    GetOrGenerateYearlyWorkloadRequest.builder()
                            .year(startDate.getYear() - 1)
                            .weeks(Set.of(week))
                            .build()
            );
        }


        return this.referenceClient.getWorkload(request).stream()
                .map(this.referenceMapper::to)
                .toList();
    }
}
