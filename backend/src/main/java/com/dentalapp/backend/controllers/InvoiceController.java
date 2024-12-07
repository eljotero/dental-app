package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.invoice.dtos.PayForAppointmentDto;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import com.dentalapp.backend.services.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/{id}/price")
    public ResponseEntity<String> createInvoice(@PathVariable Long id, @RequestBody SetAppointmentPriceDto setAppointmentPriceDto) {
        invoiceService.setAppointmentPrice(id, setAppointmentPriceDto);
        return ResponseEntity.ok("Price set successfully");
    }

    @PatchMapping("/{id}/price/update")
    public ResponseEntity<String> updateInvoice(@PathVariable Long id, @RequestBody SetAppointmentPriceDto setAppointmentPriceDto) {
        invoiceService.updateAppointmentPrice(id, setAppointmentPriceDto);
        return ResponseEntity.ok("Price updated successfully");
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<String> payInvoice(@PathVariable Long id, @RequestBody PayForAppointmentDto payForAppointmentDto) {
        invoiceService.payInvoice(id, payForAppointmentDto);
        return ResponseEntity.ok("Invoice paid successfully");
    }
}
