package com.ams.backend.service;

import com.ams.backend.dto.AvailabilityDtos.AvailabilityRequest;
import com.ams.backend.dto.AvailabilityDtos.AvailabilityResponse;
import com.ams.backend.entity.Availability;
import com.ams.backend.entity.Doctor;
import com.ams.backend.exception.ApiException;
import com.ams.backend.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final DoctorService doctorService;

    public List<AvailabilityResponse> getForDoctor(Long doctorId, boolean onlyFree) {
        List<Availability> slots = onlyFree
                ? availabilityRepository.findByDoctorIdAndBookedFalseOrderByDateAscStartTimeAsc(doctorId)
                : availabilityRepository.findByDoctorIdOrderByDateAscStartTimeAsc(doctorId);
        return slots.stream().map(this::toResponse).toList();
    }

    @Transactional
    public AvailabilityResponse add(Long doctorId, AvailabilityRequest request) {
        Doctor doctor = doctorService.findEntity(doctorId);

        Availability slot = Availability.builder()
                .doctor(doctor)
                .date(request.date())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .booked(false)
                .build();

        return toResponse(availabilityRepository.save(slot));
    }

    @Transactional
    public void delete(Long id) {
        Availability slot = availabilityRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Availability slot not found."));
        if (slot.isBooked()) {
            throw ApiException.badRequest("Cannot delete a slot that is already booked.");
        }
        availabilityRepository.deleteById(id);
    }

    Availability findEntity(Long id) {
        return availabilityRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Availability slot not found."));
    }

    private AvailabilityResponse toResponse(Availability a) {
        return new AvailabilityResponse(
                a.getId(), a.getDoctor().getId(), a.getDate(), a.getStartTime(), a.getEndTime(), a.isBooked()
        );
    }
}
