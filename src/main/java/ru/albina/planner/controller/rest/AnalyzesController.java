package ru.albina.planner.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.albina.backlib.configuration.WebConstants;
import ru.albina.backlib.configuration.auto.OpenApiConfiguration;
import ru.albina.planner.dto.response.analysis.AnalysisPerWeekDto;
import ru.albina.planner.service.AnalysisService;

@Slf4j
@RestController
@RequestMapping(WebConstants.FULL_WEB + "/analyzes")
@RequiredArgsConstructor
public class AnalyzesController {

    private final AnalysisService analysisService;

    @Operation(
            summary = "Получить анализ за производственную неделю",
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
    @GetMapping("/")
    public AnalysisPerWeekDto perWeek(
            @RequestParam("year") int year,
            @RequestParam("week") int week
    ) {
        return this.analysisService.getWeekAnalysis(year, week);
    }
}
