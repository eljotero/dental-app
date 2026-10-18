package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.invoice.dtos.PayForAppointmentDto;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import com.dentalapp.backend.model.invoice.dtos.InvoicePageResponse;
import com.dentalapp.backend.model.invoice.dtos.InvoiceQueryParams;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.services.InvoiceService;
import com.dentalapp.backend.services.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@AllArgsConstructor
public class PaymentController {

    private final InvoiceService invoiceService;

    private final PaymentService paymentService;

    @GetMapping("/all")
    public ResponseEntity<InvoicePageResponse<Invoice>> getAllInvoices(@Valid @ModelAttribute InvoiceQueryParams params) {
        return ResponseEntity.ok(invoiceService.findAllByQueryParams(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @PostMapping("/{id}/price")
    public ResponseEntity<String> createInvoice(@PathVariable Long id, @RequestBody SetAppointmentPriceDto setAppointmentPriceDto) {
        paymentService.updateAppointmentPrice(id, setAppointmentPriceDto);
        return ResponseEntity.ok("Price set successfully");
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<String> updateInvoice(@PathVariable Long id, @RequestBody SetAppointmentPriceDto setAppointmentPriceDto) {
        paymentService.updateAppointmentPrice(id, setAppointmentPriceDto);
        return ResponseEntity.ok("Price updated successfully");
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<String> payInvoice(@PathVariable Long id, @RequestBody PayForAppointmentDto payForAppointmentDto) {
        invoiceService.payInvoice(id, payForAppointmentDto);
        return ResponseEntity.ok("Appointment paid successfully");
    }
}
