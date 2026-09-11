package com.dentalapp.backend.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.appointment.dtos.*;
import com.dentalapp.backend.services.AppointmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    private final JwtService jwtService;

    @GetMapping("/all")
    public ResponseEntity<List<GetAppointmentDto>> getAppointments(@RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAppointments(date));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetAppointmentDtoV2> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentByIdDto(id));
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<GetAppointmentDtoV4> getAppointmentByIdV4(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentByIdDtoV4(id));
    }

    @GetMapping("/patient")
    public ResponseEntity<List<GetAppointmentDto>> getPatientAppointments(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(appointmentService.getPatientAppointments(jwtService.extractEmail(token.substring(7))));
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<GetAppointmentDtoV3>> getDoctorAppointments(@RequestHeader("Authorization") String token, @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(jwtService.extractEmail(token.substring(7)), date));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> createAppointment(@RequestHeader("Authorization") String token, @Valid @RequestBody CreateAppointmentDto createAppointmentDto) {
        appointmentService.createAppointment(createAppointmentDto, jwtService.extractEmail(token.substring(7)));
        return ResponseEntity.status(HttpStatus.CREATED).body("Appointment created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAppointment(@PathVariable Long id, @RequestBody UpdateAppointmentDto updateAppointmentDto, @RequestHeader("Authorization") String token) {
        appointmentService.updateAppointment(updateAppointmentDto, id, jwtService.extractEmail(token.substring(7)));
        return ResponseEntity.ok("Appointment updated");
    }

    @GetMapping("/confirm/{id}")
    public ResponseEntity<String> confirmAppointment(@PathVariable Long id) {
        appointmentService.confirmAppointment(id);
        return ResponseEntity.ok("Appointment confirmed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted");
    }
}
