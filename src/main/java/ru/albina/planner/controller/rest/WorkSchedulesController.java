package ru.albina.planner.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.albina.backlib.configuration.WebConstants;
import ru.albina.backlib.configuration.auto.OpenApiConfiguration;
import ru.albina.backlib.model.security.LibPrincipal;
import ru.albina.planner.dto.planner.PlannerDto;
import ru.albina.planner.dto.request.WorkSchedulesRequest;
import ru.albina.planner.dto.response.schedule.DayWorkSchedule;
import ru.albina.planner.service.planner.PlannerGeneratorService;
import ru.albina.planner.service.schedule.WorkScheduleUserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(WebConstants.FULL_WEB + "/work-schedules")
@RequiredArgsConstructor
public class WorkSchedulesController {

    private final WorkScheduleUserService workScheduleUserService;

    private final PlannerGeneratorService plannerGeneratorService;


    @Operation(
            summary = "Получить график за месяц",
            security = @SecurityRequirement(name = OpenApiConfiguration.JWT),
            responses = {
                    @ApiResponse(
                            description = "ОК",
                            responseCode = "200"
                    )
            }
    )
    //TODO @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<DayWorkSchedule> getWorkSchedules(
            @RequestBody WorkSchedulesRequest workSchedulesRequest
    ) {
        return this.workScheduleUserService.getDayWorkSchedules(workSchedulesRequest.getWorkScheduleDate());
    }

    @Operation(
            summary = "Получить график за месяц по себе",
            security = @SecurityRequirement(name = OpenApiConfiguration.JWT),
            responses = {
                    @ApiResponse(
                            description = "ОК",
                            responseCode = "200"
                    )
            }
    )
    //TODO @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/me")
    public List<DayWorkSchedule> getWorkSchedules(
            LibPrincipal principal,
            @RequestBody WorkSchedulesRequest workSchedulesRequest
    ) {
        return this.workScheduleUserService.getDayWorkSchedulesForDoctor(workSchedulesRequest.getWorkScheduleDate(), principal.getPrincipal().id());
    }

    @Operation(
            summary = "Сгенерировать график",
            security = @SecurityRequirement(name = OpenApiConfiguration.JWT),
            responses = {
                    @ApiResponse(
                            description = "ОК",
                            responseCode = "200"
                    )
            }
    )
    //TODO @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/generate")
    public PlannerDto generate(
            @RequestBody WorkSchedulesRequest workSchedulesRequest
    ) {
        return this.plannerGeneratorService.generateRequest(workSchedulesRequest.getWorkScheduleDate());
    }

}
