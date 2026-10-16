package com.ams.backend.config;

import com.ams.backend.entity.*;
import com.ams.backend.repository.AvailabilityRepository;
import com.ams.backend.repository.DoctorRepository;
import com.ams.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

// Seeds the same demo accounts/doctors as the frontend's mock seedData.js,
// so the two can be swapped in for each other during development.
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AvailabilityRepository availabilityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (doctorRepository.count() > 0 || userRepository.count() > 0) {
            return;
        }

        Doctor d1 = doctorRepository.save(Doctor.builder()
                .name("Dr. Nimal Silva")
                .specialization("Cardiologist")
                .qualification("MBBS, MD (Cardiology)")
                .experience(12)
                .contactNumber("0771111111")
                .description("Specialist in heart-related conditions with 12 years of experience.")
                .build());

        Doctor d2 = doctorRepository.save(Doctor.builder()
                .name("Dr. Kumari Perera")
                .specialization("Dermatologist")
                .qualification("MBBS, Dip. Dermatology")
                .experience(8)
                .contactNumber("0772222222")
                .description("Experienced dermatologist focusing on skin and hair care.")
                .build());

        Doctor d3 = doctorRepository.save(Doctor.builder()
                .name("Dr. Ashan Fernando")
                .specialization("Pediatrician")
                .qualification("MBBS, DCH")
                .experience(15)
                .contactNumber("0774444444")
                .description("Caring for children's health for over 15 years.")
                .build());

        userRepository.save(User.builder()
                .name("System Admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .contactNumber("0110000000")
                .build());

        userRepository.save(User.builder()
                .name("Dr. Nimal Silva")
                .email("silva@example.com")
                .password(passwordEncoder.encode("doctor123"))
                .role(Role.DOCTOR)
                .contactNumber("0771111111")
                .doctor(d1)
                .build());

        userRepository.save(User.builder()
                .name("Dr. Kumari Perera")
                .email("perera@example.com")
                .password(passwordEncoder.encode("doctor123"))
                .role(Role.DOCTOR)
                .contactNumber("0772222222")
                .doctor(d2)
                .build());

        userRepository.save(User.builder()
                .name("Sahan Fernando")
                .email("patient@example.com")
                .password(passwordEncoder.encode("patient123"))
                .role(Role.PATIENT)
                .contactNumber("0773333333")
                .build());

        availabilityRepository.save(Availability.builder().doctor(d1)
                .date(LocalDate.of(2026, 9, 2)).startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(9, 30)).booked(false).build());
        availabilityRepository.save(Availability.builder().doctor(d1)
                .date(LocalDate.of(2026, 9, 2)).startTime(LocalTime.of(9, 30)).endTime(LocalTime.of(10, 0)).booked(false).build());
        availabilityRepository.save(Availability.builder().doctor(d1)
                .date(LocalDate.of(2026, 9, 3)).startTime(LocalTime.of(14, 0)).endTime(LocalTime.of(14, 30)).booked(false).build());
        availabilityRepository.save(Availability.builder().doctor(d2)
                .date(LocalDate.of(2026, 9, 2)).startTime(LocalTime.of(10, 0)).endTime(LocalTime.of(10, 30)).booked(false).build());
        availabilityRepository.save(Availability.builder().doctor(d2)
                .date(LocalDate.of(2026, 9, 4)).startTime(LocalTime.of(15, 0)).endTime(LocalTime.of(15, 30)).booked(false).build());
        availabilityRepository.save(Availability.builder().doctor(d3)
                .date(LocalDate.of(2026, 9, 5)).startTime(LocalTime.of(11, 0)).endTime(LocalTime.of(11, 30)).booked(false).build());
    }
}
