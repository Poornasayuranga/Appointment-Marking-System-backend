package com.ams.backend.repository;

import com.ams.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientIdOrderByDateAscStartTimeAsc(Long patientId);
    List<Appointment> findByDoctorIdOrderByDateAscStartTimeAsc(Long doctorId);
    List<Appointment> findAllByOrderByDateDescStartTimeDesc();
    long countByDoctorId(Long doctorId);
}
