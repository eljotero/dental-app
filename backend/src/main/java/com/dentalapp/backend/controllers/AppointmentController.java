package com.dentalapp.backend.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.appointment.dtos.CreateAppointmentDto;
import com.dentalapp.backend.model.appointment.dtos.UpdateAppointmentDto;
import com.dentalapp.backend.services.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    private final JwtService jwtService;

    public AppointmentController(AppointmentService appointmentService, JwtService jwtService) {
        this.appointmentService = appointmentService;
        this.jwtService = jwtService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAppointments() {
        return ResponseEntity.ok(appointmentService.getAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/patient")
    public ResponseEntity<?> getPatientAppointments(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.substring(7));
        return ResponseEntity.ok(appointmentService.getPatientAppointments(email));
    }

    @GetMapping("/doctor")
    public ResponseEntity<?> getDoctorAppointments(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.substring(7));
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(email));
    }

    @GetMapping("/doctor/date")
    public ResponseEntity<?> getDoctorAppointmentsByDate(@RequestHeader("Authorization") String token, LocalDate date) {
        String email = jwtService.extractEmail(token.substring(7));
        return ResponseEntity.ok(appointmentService.getDoctorAppointmentsByDate(email, date));
    }

    @GetMapping("/date")
    public ResponseEntity<?> getAppointmentsByDate(LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDate(date));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled");
    }

    @PostMapping("/add")
    public ResponseEntity<?> createAppointment(@RequestHeader("Authorization") String token, @Valid @RequestBody CreateAppointmentDto createAppointmentDto) {
        appointmentService.createAppointment(createAppointmentDto, jwtService.extractEmail(token.substring(7)));
        return ResponseEntity.ok("Appointment created");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody UpdateAppointmentDto updateAppointmentDto) {
        appointmentService.updateAppointment(updateAppointmentDto, id);
        return ResponseEntity.ok("Appointment updated");
    }

    @GetMapping("/confirm/{id}")
    public ResponseEntity<?> confirmAppointment(@PathVariable Long id) {
        appointmentService.confirmAppointment(id);
        return ResponseEntity.ok("Appointment confirmed");
    }
}
