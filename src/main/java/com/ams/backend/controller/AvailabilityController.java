package com.ams.backend.controller;

import com.ams.backend.dto.AvailabilityDtos.AvailabilityRequest;
import com.ams.backend.dto.AvailabilityDtos.AvailabilityResponse;
import com.ams.backend.service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @GetMapping("/doctor/{doctorId}")
    public List<AvailabilityResponse> getForDoctor(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "false") boolean onlyFree
    ) {
        return availabilityService.getForDoctor(doctorId, onlyFree);
    }

    @PostMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<AvailabilityResponse> add(
            @PathVariable Long doctorId,
            @Valid @RequestBody AvailabilityRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(availabilityService.add(doctorId, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        availabilityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
