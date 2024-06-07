package ru.albina.planner.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.albina.backlib.configuration.WebConstants;
import ru.albina.backlib.configuration.auto.OpenApiConfiguration;
import ru.albina.planner.dto.planner.PlannerDto;
import ru.albina.planner.dto.request.WorkSchedulesRequest;
import ru.albina.planner.service.planner.PlannerGeneratorService;

@Slf4j
@RestController
@RequestMapping(WebConstants.FULL_WEB + "/work-schedules/")
@RequiredArgsConstructor
public class WorkSchedulesController {

    private final PlannerGeneratorService plannerGeneratorService;


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
