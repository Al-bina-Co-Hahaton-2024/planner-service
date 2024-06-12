package ru.albina.planner.service.planner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    /**
     * Get hours for some task
     *
     * @param load        number of tasks.
     * @param performance performance for task.
     * @param maxHours    max hours.
     * @return hours.
     */
    public Double delegatePerformance(long load, long performance, double maxHours) {
        var hours = 0d;
        //Not quite perform
        while (load > 0 && hours <= maxHours) {
            if (load >= performance) {
                hours++;
            } else {
                hours += new BigDecimal(1 / 5d, new MathContext(1, RoundingMode.HALF_DOWN)).doubleValue();
            }
            load -= performance;
        }

        return hours;
    }

    public long calculatePerformance(long performance, double hours) {
        return (long) Math.ceil(performance * hours);
    }
}
