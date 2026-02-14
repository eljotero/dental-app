package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.invoice.dtos.InvoiceMapper;
import com.dentalapp.backend.model.invoice.dtos.PayForAppointmentDto;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.model.invoice.exceptions.InvoiceNotFoundException;
import com.dentalapp.backend.model.invoice.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    public List<Invoice> findAllByQueryParams(Specification<Invoice> spec) {
        return invoiceRepository.findAll(spec);
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
    public void setAppointmentPrice(Appointment appointment, SetAppointmentPriceDto setAppointmentPriceDto) {
        appointment.getInvoice().setPrice(setAppointmentPriceDto.getPrice());
        invoiceRepository.save(appointment.getInvoice());
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
