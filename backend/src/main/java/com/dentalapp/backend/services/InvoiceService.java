package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.invoice.dtos.InvoiceMapper;
import com.dentalapp.backend.model.invoice.dtos.InvoicePageResponse;
import com.dentalapp.backend.model.invoice.dtos.InvoiceQueryParams;
import com.dentalapp.backend.model.invoice.dtos.PayForAppointmentDto;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.model.invoice.exceptions.InvoiceNotFoundException;
import com.dentalapp.backend.model.invoice.repository.InvoiceRepository;
import com.dentalapp.backend.model.invoice.repository.InvoiceSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    public InvoicePageResponse<Invoice> findAllByQueryParams(InvoiceQueryParams params) {
        return InvoicePageResponse.from(invoiceRepository.findAll(new InvoiceSpecification(params), params.toPageable()));
    }

    public Invoice findById(Long id) {
        return invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));
    }

    @Transactional
    public Invoice createInvoice() {
        Invoice invoice = new Invoice();
        invoiceRepository.save(invoice);
        return invoice;
    }

    @Transactional
    public void updateAppointmentPrice(Appointment appointment, SetAppointmentPriceDto setAppointmentPriceDto) {
        appointment.getInvoice().setPrice(setAppointmentPriceDto.getPrice());
        invoiceRepository.save(appointment.getInvoice());
    }

    @Transactional
    public void payInvoice(Long id, PayForAppointmentDto payForAppointmentDto) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));
        boolean isPriceSet = invoice.getPrice() != null;
        Invoice invoiceDB = invoiceMapper.toDto(invoice, payForAppointmentDto, isPriceSet);
        invoiceRepository.save(invoiceDB);
    }

    @Transactional
    public void saveInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

}
