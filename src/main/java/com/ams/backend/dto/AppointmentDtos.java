package com.ams.backend.dto;

import com.ams.backend.entity.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDtos {

    public record BookAppointmentRequest(
            @NotNull Long doctorId,
            @NotNull Long availabilityId,
            String reason
    ) {}

    public record UpdateStatusRequest(
            @NotNull AppointmentStatus status
    ) {}

    public record AppointmentResponse(
            Long id,
            Long patientId,
            String patientName,
            Long doctorId,
            String doctorName,
            Long availabilityId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            AppointmentStatus status,
            String reason,
            Instant createdAt
    ) {}
}
