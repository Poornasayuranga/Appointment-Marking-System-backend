package com.ams.backend.service;

import com.ams.backend.dto.AppointmentDtos.AppointmentResponse;
import com.ams.backend.dto.AppointmentDtos.BookAppointmentRequest;
import com.ams.backend.entity.Appointment;
import com.ams.backend.entity.AppointmentStatus;
import com.ams.backend.entity.Availability;
import com.ams.backend.entity.Doctor;
import com.ams.backend.entity.User;
import com.ams.backend.exception.ApiException;
import com.ams.backend.repository.AppointmentRepository;
import com.ams.backend.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final AvailabilityService availabilityService;
    private final DoctorService doctorService;

    @Transactional
    public synchronized AppointmentResponse book(User patient, BookAppointmentRequest request) {
        Doctor doctor = doctorService.findEntity(request.doctorId());
        Availability slot = availabilityService.findEntity(request.availabilityId());

        if (!slot.getDoctor().getId().equals(doctor.getId())) {
            throw ApiException.badRequest("This slot does not belong to the selected doctor.");
        }
        if (slot.isBooked()) {
            throw ApiException.conflict("Sorry, this time slot is no longer available.");
        }

        slot.setBooked(true);
        availabilityRepository.save(slot);

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .availability(slot)
                .date(slot.getDate())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .status(AppointmentStatus.PENDING)
                .reason(request.reason())
                .build();

        return toResponse(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> getForPatient(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByDateAscStartTimeAsc(patientId).stream()
                .map(this::toResponse).toList();
    }

    public List<AppointmentResponse> getForDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByDateAscStartTimeAsc(doctorId).stream()
                .map(this::toResponse).toList();
    }

    public List<AppointmentResponse> getAll() {
        return appointmentRepository.findAllByOrderByDateDescStartTimeDesc().stream()
                .map(this::toResponse).toList();
    }

    @Transactional
    public AppointmentResponse updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Appointment not found."));

        appointment.setStatus(status);

        if (status == AppointmentStatus.REJECTED || status == AppointmentStatus.CANCELLED) {
            Availability slot = appointment.getAvailability();
            slot.setBooked(false);
            availabilityRepository.save(slot);
        }

        return toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse cancel(Long id) {
        return updateStatus(id, AppointmentStatus.CANCELLED);
    }

    private AppointmentResponse toResponse(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getPatient().getId(),
                a.getPatient().getName(),
                a.getDoctor().getId(),
                a.getDoctor().getName(),
                a.getAvailability().getId(),
                a.getDate(),
                a.getStartTime(),
                a.getEndTime(),
                a.getStatus(),
                a.getReason(),
                a.getCreatedAt()
        );
    }
}
