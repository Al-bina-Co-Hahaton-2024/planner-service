package ru.albina.planner.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import ru.albina.planner.dto.request.PerformanceAnalysisRequest;
import ru.albina.planner.dto.response.analysis.AnalysisPerWeekDto;
import ru.albina.planner.dto.response.performance.PerformanceAnalysis;
import ru.albina.planner.service.analysis.PerformanceAnalysisService;

@Slf4j
@RestController
@RequestMapping(WebConstants.FULL_WEB + "/performance-analyzes")
@RequiredArgsConstructor
public class PerformanceAnalyzesController {

    private final PerformanceAnalysisService performanceAnalysisService;

    @Operation(
            summary = "Расчитать производительность сотрудника для модальности",
            security = @SecurityRequirement(name = OpenApiConfiguration.JWT),
            responses = {
                    @ApiResponse(
                            description = "ОК",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = AnalysisPerWeekDto.class))
                    )
            }
    )
    //TODO @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/perform")
    public PerformanceAnalysis perform(
            @RequestBody PerformanceAnalysisRequest performanceAnalysisRequest
    ) {
        return this.performanceAnalysisService.perform(performanceAnalysisRequest);
    }
}
