package com.dentalapp.backend.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.appointment.dtos.*;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.services.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<Appointment>> getAppointments(@RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAppointments(date));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') or @appointmentService.isUsersAppointment(#id, authentication.principal.username)")
    public ResponseEntity<GetAppointmentDtoV2> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentByIdDto(id));
    }

    @GetMapping("/doctor/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<GetAppointmentDtoV4> getAppointmentByIdV4(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentByIdDtoV4(id));
    }

    @GetMapping("/patient")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<GetAppointmentDto>> getPatientAppointments(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(jwtService.extractEmail(token.substring(7))));
    }

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<GetAppointmentDtoV3>> getDoctorAppointments(@RequestHeader("Authorization") String token, @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(jwtService.extractEmail(token.substring(7)), date));
    }

    @PostMapping("/cancel/{id}")
    @PreAuthorize("@appointmentService.isUsersAppointment(#id, authentication.principal.username)")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled");
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createAppointment(@RequestHeader("Authorization") String token, @Valid @RequestBody CreateAppointmentDto createAppointmentDto) {
        appointmentService.createAppointment(createAppointmentDto, jwtService.extractEmail(token.substring(7)));
        return ResponseEntity.status(HttpStatus.CREATED).body("Appointment created");
    }

    @PutMapping("/{id}")
    @PreAuthorize("@appointmentService.isUsersAppointment(#id, authentication.principal.username)")
    public ResponseEntity<String> updateAppointment(@PathVariable Long id, @RequestBody UpdateAppointmentDto updateAppointmentDto, @RequestHeader("Authorization") String token) {
        appointmentService.updateAppointment(updateAppointmentDto, id, jwtService.extractEmail(token.substring(7)));
        return ResponseEntity.ok("Appointment updated");
    }

    @GetMapping("/confirm/{id}")
    @PreAuthorize("@appointmentService.isUsersAppointment(#id, authentication.principal.username)")
    public ResponseEntity<String> confirmAppointment(@PathVariable Long id) {
        appointmentService.confirmAppointment(id);
        return ResponseEntity.ok("Appointment confirmed");
    }
}
