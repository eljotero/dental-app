package com.dentalapp.backend.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.appointment.dtos.CreateAppointmentDto;
import com.dentalapp.backend.model.appointment.dtos.UpdateAppointmentDto;
import com.dentalapp.backend.services.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;

    private final JwtService jwtService;

    public AppointmentController(AppointmentService appointmentService, JwtService jwtService) {
        this.appointmentService = appointmentService;
        this.jwtService = jwtService;
    }

    @GetMapping("/appointments")
    public ResponseEntity<?> getAppointments() {
        return ResponseEntity.ok(appointmentService.getAppointments());
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/appointments/patient")
    public ResponseEntity<?> getPatientAppointments(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.substring(7));
        return ResponseEntity.ok(appointmentService.getPatientAppointments(email));
    }

    @GetMapping("/appointments/doctor")
    public ResponseEntity<?> getDoctorAppointments(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.substring(7));
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(email));
    }

    @GetMapping("/appointments/doctor/date")
    public ResponseEntity<?> getDoctorAppointmentsByDate(@RequestHeader("Authorization") String token, String date) {
        String email = jwtService.extractEmail(token.substring(7));
        return ResponseEntity.ok(appointmentService.getDoctorAppointmentsByDate(email, date));
    }

    @GetMapping("/appointments/date")
    public ResponseEntity<?> getAppointmentsByDate(String date) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDate(date));
    }

    @PostMapping("/appointments/cancel/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled");
    }

    @PostMapping("/appointments")
    public ResponseEntity<?> createAppointment(@RequestBody CreateAppointmentDto createAppointmentDto) {
        appointmentService.createAppointment(createAppointmentDto);
        return ResponseEntity.ok("Appointment created");
    }

    @PatchMapping("/appointments/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody UpdateAppointmentDto updateAppointmentDto) {
        appointmentService.updateAppointment(updateAppointmentDto, id);
        return ResponseEntity.ok("Appointment updated");
    }
}
