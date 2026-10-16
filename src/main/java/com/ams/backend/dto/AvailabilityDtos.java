package com.ams.backend.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailabilityDtos {

    public record AvailabilityRequest(
            @NotNull LocalDate date,
            @NotNull LocalTime startTime,
            @NotNull LocalTime endTime
    ) {}

    public record AvailabilityResponse(
            Long id,
            Long doctorId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            boolean booked
    ) {}
}
