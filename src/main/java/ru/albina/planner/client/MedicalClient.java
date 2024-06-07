package ru.albina.planner.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.albina.backlib.configuration.WebConstants;
import ru.albina.planner.dto.medical.Doctor;

import java.util.List;
import java.util.Optional;

@Component
public class MedicalClient {

    private final WebClient webClient;

    public MedicalClient(WebClient.Builder libWebClientBuilder) {
        this.webClient = libWebClientBuilder
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
}
