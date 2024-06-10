package ru.albina.planner.service.planner.scheduler;

import ru.albina.planner.dto.planner.PlannerDto;
import ru.albina.planner.dto.response.data.DoctorDayDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DistributorService {


    Map<LocalDate, List<DoctorDayDto>> distributeDoctors(PlannerDto planner);
}
