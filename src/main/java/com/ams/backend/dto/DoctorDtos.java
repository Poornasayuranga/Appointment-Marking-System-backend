package com.ams.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class DoctorDtos {

    public record DoctorRequest(
            @NotBlank String name,
            @NotBlank String specialization,
            String qualification,
            Integer experience,
            String contactNumber,
            String description
    ) {}

    public record DoctorResponse(
            Long id,
            String name,
            String specialization,
            String qualification,
            Integer experience,
            String contactNumber,
            String description
    ) {}
}
