package ru.albina.planner.dto.reference;

import lombok.Data;

@Data
public class HoursPerMonthValue {

    private int year;

    private int month;

    private double hours;
}
