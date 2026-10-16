package com.ams.backend.dto;

import java.util.List;
import java.util.Map;

public class AdminDtos {

    public record MonthCount(String month, long count) {}

    public record DoctorCount(String doctor, long count) {}

    public record AdminStatsResponse(
            long totalDoctors,
            long totalPatients,
            long totalAppointments,
            Map<String, Long> statusCounts,
            List<MonthCount> appointmentsByMonth,
            List<DoctorCount> appointmentsByDoctor
    ) {}

    public record PatientResponse(
            Long id,
            String name,
            String email,
            String contactNumber
    ) {}
}
