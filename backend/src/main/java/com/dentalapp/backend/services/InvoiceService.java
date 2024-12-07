package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.invoice.dtos.InvoiceMapper;
import com.dentalapp.backend.model.invoice.dtos.PayForAppointmentDto;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.model.invoice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final AppointmentService appointmentService;

    public InvoiceService(InvoiceRepository invoiceRepository, AppointmentService appointmentService) {
        this.invoiceRepository = invoiceRepository;
        this.appointmentService = appointmentService;
    }

    @Transactional
    public Invoice createInvoice() {
        Invoice invoice = new Invoice();
        invoiceRepository.save(invoice);
        return invoice;
    }

    @Transactional
    public void setAppointmentPrice(Long id, SetAppointmentPriceDto setAppointmentPriceDto) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        appointment.getInvoice().setPrice(setAppointmentPriceDto.getPrice());
        invoiceRepository.save(appointment.getInvoice());
    }

    @Transactional
    public void updateAppointmentPrice(Long id, SetAppointmentPriceDto setAppointmentPriceDto) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        appointment.getInvoice().setPrice(setAppointmentPriceDto.getPrice());
        invoiceRepository.save(appointment.getInvoice());
    }

    @Transactional
    public void payInvoice(Long id, PayForAppointmentDto payForAppointmentDto) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        Invoice invoiceDB = InvoiceMapper.toDto(invoice, payForAppointmentDto);
        invoiceRepository.save(invoiceDB);
    }

}
