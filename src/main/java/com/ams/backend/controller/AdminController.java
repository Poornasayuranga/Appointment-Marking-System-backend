package com.ams.backend.controller;

import com.ams.backend.dto.AdminDtos.AdminStatsResponse;
import com.ams.backend.dto.AdminDtos.PatientResponse;
import com.ams.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    public AdminStatsResponse getStats() {
        return adminService.getStats();
    }

    @GetMapping("/patients")
    public List<PatientResponse> getAllPatients() {
        return adminService.getAllPatients();
    }
}
