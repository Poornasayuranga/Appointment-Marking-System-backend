package com.ams.backend.controller;

import com.ams.backend.dto.AppointmentDtos.AppointmentResponse;
import com.ams.backend.dto.AppointmentDtos.BookAppointmentRequest;
import com.ams.backend.dto.AppointmentDtos.UpdateStatusRequest;
import com.ams.backend.entity.User;
import com.ams.backend.security.UserPrincipal;
import com.ams.backend.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponse> book(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody BookAppointmentRequest request
    ) {
        User patient = principal.getUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.book(patient, request));
    }

    @GetMapping("/patient/{patientId}")
    public List<AppointmentResponse> getForPatient(@PathVariable Long patientId) {
        return appointmentService.getForPatient(patientId);
    }

    @GetMapping("/doctor/{doctorId}")
    public List<AppointmentResponse> getForDoctor(@PathVariable Long doctorId) {
        return appointmentService.getForDoctor(doctorId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppointmentResponse> getAll() {
        return appointmentService.getAll();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public AppointmentResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {
        return appointmentService.updateStatus(id, request.status());
    }

    @PatchMapping("/{id}/cancel")
    public AppointmentResponse cancel(@PathVariable Long id) {
        return appointmentService.cancel(id);
    }
}
