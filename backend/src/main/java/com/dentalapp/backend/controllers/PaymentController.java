package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.invoice.dtos.PayForAppointmentDto;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final AppointmentService appointmentService;

    private final InvoiceService invoiceService;

    public PaymentController(AppointmentService appointmentService, InvoiceService invoiceService) {
        this.appointmentService = appointmentService;
        this.invoiceService = invoiceService;
    }

    @PostMapping("/{id}/price")
    public ResponseEntity<String> createInvoice(@PathVariable Long id, @RequestBody SetAppointmentPriceDto setAppointmentPriceDto) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        invoiceService.setAppointmentPrice(appointment, setAppointmentPriceDto);
        return ResponseEntity.ok("Price set successfully");
    }

    @PatchMapping("/{id}/price/update")
    public ResponseEntity<String> updateInvoice(@PathVariable Long id, @RequestBody SetAppointmentPriceDto setAppointmentPriceDto) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        invoiceService.updateAppointmentPrice(appointment, setAppointmentPriceDto);
        return ResponseEntity.ok("Price updated successfully");
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<String> payInvoice(@PathVariable Long id, @RequestBody PayForAppointmentDto payForAppointmentDto) {
        invoiceService.payInvoice(id, payForAppointmentDto);
        return ResponseEntity.ok("Appointment paid successfully");
    }
}
