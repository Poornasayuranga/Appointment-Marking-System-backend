package com.ams.backend.service;

import com.ams.backend.dto.DoctorDtos.DoctorRequest;
import com.ams.backend.dto.DoctorDtos.DoctorResponse;
import com.ams.backend.entity.Doctor;
import com.ams.backend.exception.ApiException;
import com.ams.backend.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<DoctorResponse> search(String search, String specialization) {
        return doctorRepository.search(search, specialization).stream()
                .map(this::toResponse)
                .toList();
    }

    public DoctorResponse getById(Long id) {
        return toResponse(findEntity(id));
    }

    Doctor findEntity(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Doctor not found."));
    }

    @Transactional
    public DoctorResponse create(DoctorRequest request) {
        Doctor doctor = Doctor.builder()
                .name(request.name())
                .specialization(request.specialization())
                .qualification(request.qualification())
                .experience(request.experience())
                .contactNumber(request.contactNumber())
                .description(request.description())
                .build();

        return toResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor doctor = findEntity(id);
        doctor.setName(request.name());
        doctor.setSpecialization(request.specialization());
        doctor.setQualification(request.qualification());
        doctor.setExperience(request.experience());
        doctor.setContactNumber(request.contactNumber());
        doctor.setDescription(request.description());
        return toResponse(doctor);
    }

    @Transactional
    public void delete(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw ApiException.notFound("Doctor not found.");
        }
        doctorRepository.deleteById(id);
    }

    private DoctorResponse toResponse(Doctor d) {
        return new DoctorResponse(
                d.getId(), d.getName(), d.getSpecialization(), d.getQualification(),
                d.getExperience(), d.getContactNumber(), d.getDescription()
        );
    }
}
