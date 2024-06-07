package ru.albina.planner.service.planner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.albina.planner.client.MedicalClient;
import ru.albina.planner.dto.planner.DoctorDto;
import ru.albina.planner.mapper.MedicalMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerDoctorService {

    private final MedicalClient medicalClient;

    private final MedicalMapper medicalMapper;


    public List<DoctorDto> generateDoctors() {
        return this.medicalClient.getDoctors().stream()
                .map(this.medicalMapper::from)
                .toList();
    }
}
