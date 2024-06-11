package ru.albina.planner.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.albina.backlib.configuration.WebConstants;
import ru.albina.planner.dto.response.schedule.DayWorkSchedule;
import ru.albina.planner.service.schedule.WorkScheduleUserService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(WebConstants.FULL_PRIVATE + "/work-schedules")
@RequiredArgsConstructor
public class WorkSchedulesInternalController {

    private final WorkScheduleUserService workScheduleUserService;


    @Operation(
            summary = "Получить график за месяц",
            responses = {
                    @ApiResponse(
                            description = "ОК",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping
    public List<DayWorkSchedule> getWorkSchedules(
            @RequestParam("date") LocalDate workScheduleDate
    ) {
        return this.workScheduleUserService.getDayWorkSchedules(workScheduleDate);
    }

}
