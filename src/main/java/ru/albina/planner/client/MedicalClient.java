package ru.albina.planner.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import ru.albina.backlib.configuration.WebConstants;
import ru.albina.planner.dto.medical.Doctor;
import ru.albina.planner.dto.medical.Performance;

import java.util.List;
import java.util.Optional;

import static ru.albina.planner.configuration.CacheConfiguration.REFERENCE_CACHE_NAME;

@Component
public class MedicalClient {

    private final WebClient webClient;

    public MedicalClient(WebClient.Builder libWebClientBuilder) {
        final int size = 30 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();

        this.webClient = libWebClientBuilder
                .exchangeStrategies(strategies)
                .baseUrl(Optional.ofNullable(System.getenv("MEDICAL_SERVICE_HOST")).orElse("http://localhost:8081"))
                .build();
    }


    public List<Doctor> getDoctors() {
        return this.webClient.get()
                .uri(WebConstants.FULL_PRIVATE + "/doctors")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Doctor>>() {
                })
                .block();
    }

    @Cacheable(REFERENCE_CACHE_NAME)
    public List<Performance> getAveragePerformances() {
        return this.webClient.get()
                .uri(WebConstants.FULL_PRIVATE + "/performances/average")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Performance>>() {
                })
                .block();
    }
}
