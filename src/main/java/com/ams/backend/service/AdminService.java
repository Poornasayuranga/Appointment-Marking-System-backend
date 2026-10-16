package com.ams.backend.service;

import com.ams.backend.dto.AdminDtos.AdminStatsResponse;
import com.ams.backend.dto.AdminDtos.DoctorCount;
import com.ams.backend.dto.AdminDtos.MonthCount;
import com.ams.backend.dto.AdminDtos.PatientResponse;
import com.ams.backend.entity.Appointment;
import com.ams.backend.entity.AppointmentStatus;
import com.ams.backend.entity.Doctor;
import com.ams.backend.entity.Role;
import com.ams.backend.repository.AppointmentRepository;
import com.ams.backend.repository.DoctorRepository;
import com.ams.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public AdminStatsResponse getStats() {
        List<Doctor> doctors = doctorRepository.findAll();
        long totalPatients = userRepository.findByRole(Role.PATIENT).size();
        List<Appointment> appointments = appointmentRepository.findAll();

        Map<String, Long> statusCounts = new LinkedHashMap<>();
        for (AppointmentStatus status : AppointmentStatus.values()) {
            statusCounts.put(status.name(), 0L);
        }
        for (Appointment a : appointments) {
            statusCounts.merge(a.getStatus().name(), 1L, Long::sum);
        }

        Map<String, Long> monthCounts = new TreeMap<>();
        for (Appointment a : appointments) {
            String month = a.getDate().toString().substring(0, 7);
            monthCounts.merge(month, 1L, Long::sum);
        }
        List<MonthCount> appointmentsByMonth = monthCounts.entrySet().stream()
                .map(e -> new MonthCount(e.getKey(), e.getValue()))
                .toList();

        List<DoctorCount> appointmentsByDoctor = doctors.stream()
                .map(d -> new DoctorCount(d.getName(), appointmentRepository.countByDoctorId(d.getId())))
                .toList();

        return new AdminStatsResponse(
                doctors.size(),
                totalPatients,
                appointments.size(),
                statusCounts,
                appointmentsByMonth,
                appointmentsByDoctor
        );
    }

    public List<PatientResponse> getAllPatients() {
        return userRepository.findByRole(Role.PATIENT).stream()
                .map(u -> new PatientResponse(u.getId(), u.getName(), u.getEmail(), u.getContactNumber()))
                .toList();
    }
}
