package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.invoice.dtos.PayForAppointmentDto;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.services.InvoiceService;
import com.dentalapp.backend.services.PaymentService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final InvoiceService invoiceService;

    private final PaymentService paymentService;

    public PaymentController(InvoiceService invoiceService, PaymentService paymentService) {
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Invoice>> getAllInvoices(@RequestParam(required = false) String status, @RequestParam(required = false) String method, @RequestParam(required = false) LocalDate date) {
        Specification<Invoice> specification = Specification.where(null);
        if(status != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentStatus"), status));
        }
        if(method != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentMethod"), method));
        }
        if(date != null) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentDate"), date));
        }
        return ResponseEntity.ok(invoiceService.findAllByQueryParams(specification));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @PostMapping("/{id}/price")
    public ResponseEntity<String> createInvoice(@PathVariable Long id, @RequestBody SetAppointmentPriceDto setAppointmentPriceDto) {
        paymentService.setAppointmentPrice(id, setAppointmentPriceDto);
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
