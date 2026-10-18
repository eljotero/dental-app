package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.appointment.dtos.*;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.AppointmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<AppointmentPageResponse<GetAppointmentDto>> getAppointments(@Valid @ModelAttribute AppointmentQueryParams params) {
        return ResponseEntity.ok(appointmentService.getAppointments(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDetailsResponse> getAppointmentById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(appointmentService.getAppointmentDetails(id, user.getUserId(), user.getUserType()));
    }

    @GetMapping("/me")
    public ResponseEntity<AppointmentPageResponse<MyAppointmentResponse>> getMyAppointments(@AuthenticationPrincipal User user, @Valid @ModelAttribute MyAppointmentsQueryParams params) {
        return ResponseEntity.ok(appointmentService.getMyAppointments(user.getUserId(), user.getUserType(), params));
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok("Appointment cancelled");
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> createAppointment(@AuthenticationPrincipal User user, @Valid @RequestBody CreateAppointmentDto createAppointmentDto) {
        appointmentService.createAppointment(createAppointmentDto, user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body("Appointment created");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAppointment(@PathVariable Long id, @RequestBody UpdateAppointmentDto updateAppointmentDto, @AuthenticationPrincipal User user) {
        appointmentService.updateAppointment(updateAppointmentDto, id, user.getEmail());
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
