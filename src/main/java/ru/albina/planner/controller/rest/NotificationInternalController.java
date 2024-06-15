package ru.albina.planner.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.albina.backlib.configuration.WebConstants;
import ru.albina.planner.dto.request.PlannerOutDateDaysNotification;
import ru.albina.planner.service.notification.NotificationOutDatedService;

@Slf4j
@RestController
@RequestMapping(WebConstants.FULL_PRIVATE + "/notifications/out-dates")
@RequiredArgsConstructor
public class NotificationInternalController {


    private final NotificationOutDatedService notification;

    @Operation(
            summary = "Сообщить об изменении в графике",
            responses = {
                    @ApiResponse(
                            description = "ОК",
                            responseCode = "200"
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void getWorkSchedules(
            @RequestBody PlannerOutDateDaysNotification planner
    ) {
        this.notification.notifyOutDates(planner);
    }
}
