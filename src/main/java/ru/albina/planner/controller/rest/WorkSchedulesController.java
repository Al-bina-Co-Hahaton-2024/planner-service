package ru.albina.planner.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.albina.backlib.configuration.WebConstants;
import ru.albina.backlib.configuration.auto.OpenApiConfiguration;
import ru.albina.backlib.model.security.LibPrincipal;
import ru.albina.planner.dto.request.DoctorSchedulesEditRequest;
import ru.albina.planner.dto.request.GetWorkSchedulesRequest;
import ru.albina.planner.dto.request.WorkSchedulesRequest;
import ru.albina.planner.dto.response.schedule.DayWorkSchedule;
import ru.albina.planner.service.planner.PlannerRunner;
import ru.albina.planner.service.schedule.WorkScheduleEditService;
import ru.albina.planner.service.schedule.WorkScheduleUserService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(WebConstants.FULL_WEB + "/work-schedules")
@RequiredArgsConstructor
public class WorkSchedulesController {

    private final WorkScheduleEditService workScheduleEditService;

    private final WorkScheduleUserService workScheduleUserService;

    private final PlannerRunner plannerRunner;


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
            @RequestParam("date") LocalDate workScheduleDate
    ) {
        return this.workScheduleUserService.getDayWorkSchedulesWithProduction(workScheduleDate);
    }

    @Operation(
            summary = "Получить график за месяц с фильтрами",
            security = @SecurityRequirement(name = OpenApiConfiguration.JWT),
            responses = {
                    @ApiResponse(
                            description = "ОК",
                            responseCode = "200"
                    )
            }
    )
    //TODO @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/find")
    public List<DayWorkSchedule> getWorkSchedules(
            @RequestBody GetWorkSchedulesRequest request
    ) {
        return this.workScheduleUserService.getDayWorkSchedulesWithProductionWithFilter(request);
    }

    @Operation(
            summary = "Изменить дополнительные часы для графика",
            security = @SecurityRequirement(name = OpenApiConfiguration.JWT),
            responses = {
                    @ApiResponse(
                            description = "ОК",
                            responseCode = "200"
                    )
            }
    )
    //TODO @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id-work-scheduler}/doctor-schedules/{id-doctor-scheduler}/extra-hours")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void editExtraHours(
            @PathVariable("id-work-scheduler") UUID workSchedulerId,
            @PathVariable("id-doctor-scheduler") UUID doctorSchedulerId,
            @RequestBody DoctorSchedulesEditRequest doctorSchedulesEditRequest
    ) {
        this.workScheduleEditService.extraHours(
                workSchedulerId,
                doctorSchedulerId,
                doctorSchedulesEditRequest.getExtraHours()
        );
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
            @RequestParam("date") LocalDate workScheduleDate
    ) {
        return this.workScheduleUserService.getDayWorkSchedulesForDoctor(workScheduleDate, principal.getPrincipal().id());
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
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void generate(
            @RequestBody WorkSchedulesRequest workSchedulesRequest
    ) {
        this.plannerRunner.run(workSchedulesRequest.getWorkScheduleDate());
    }

}
