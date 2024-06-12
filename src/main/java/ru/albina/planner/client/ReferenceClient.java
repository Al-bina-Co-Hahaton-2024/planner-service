package ru.albina.planner.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.albina.backlib.configuration.WebConstants;
import ru.albina.planner.dto.reference.GetOrGenerateYearlyWorkloadRequest;
import ru.albina.planner.dto.reference.HoursPerMonthValue;
import ru.albina.planner.dto.reference.WeekNumberResult;
import ru.albina.planner.dto.reference.Workload;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.albina.planner.configuration.CacheConfiguration.REFERENCE_CACHE_NAME;

@Component
public class ReferenceClient {

    private final WebClient webClient;

    public ReferenceClient(WebClient.Builder libWebClientBuilder) {
        this.webClient = libWebClientBuilder
                .baseUrl(Optional.ofNullable(System.getenv("REFERENCE_SERVICE_HOST")).orElse("http://localhost:8083"))
                .build();
    }


    public List<Workload> getWorkload(List<GetOrGenerateYearlyWorkloadRequest> request) {
        return this.webClient.post()
                .uri(WebConstants.FULL_PRIVATE + "/workloads/calculate")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Workload>>() {
                })
                .block();
    }

    @Cacheable(REFERENCE_CACHE_NAME)
    public double getHours(int year, int month) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path(WebConstants.FULL_PRIVATE + "/hours-per-month/{year}/months/{month}").build(year, month))
                .retrieve()
                .bodyToMono(HoursPerMonthValue.class)
                .block()
                .getHours();
    }

    @Cacheable(REFERENCE_CACHE_NAME)
    public WeekNumberResult getWeek(LocalDate localDate) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path(WebConstants.FULL_PRIVATE + "/week-numbers/").queryParam("date", localDate).build())
                .retrieve()
                .bodyToMono(WeekNumberResult.class)
                .block();
    }

    @Cacheable(REFERENCE_CACHE_NAME)
    public WeekNumberResult getWeek(int year, int week) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path(WebConstants.FULL_PRIVATE + "/week-numbers/")
                        .queryParam("year", year)
                        .queryParam("week", week)
                        .build())
                .retrieve()
                .bodyToMono(WeekNumberResult.class)
                .block();
    }

    @Cacheable(REFERENCE_CACHE_NAME)
    public List<WeekNumberResult> getWeeks(Collection<LocalDate> localDates) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path(WebConstants.FULL_PRIVATE + "/week-numbers").queryParam("dates", localDates).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<WeekNumberResult>>() {
                })
                .block();
    }
}
