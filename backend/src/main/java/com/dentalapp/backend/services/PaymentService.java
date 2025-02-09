package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.invoice.dtos.PayForAppointmentDto;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final AppointmentService appointmentService;

    private final InvoiceService invoiceService;

    public PaymentService(AppointmentService appointmentService, InvoiceService invoiceService) {
        this.appointmentService = appointmentService;
        this.invoiceService = invoiceService;
    }

    public void setAppointmentPrice(Long id, SetAppointmentPriceDto setAppointmentPriceDto) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        invoiceService.setAppointmentPrice(appointment, setAppointmentPriceDto);
        invoiceService.saveInvoice(appointment.getInvoice());
        appointmentService.saveAppointment(appointment);
    }

    public void updateAppointmentPrice(Long id, SetAppointmentPriceDto setAppointmentPriceDto) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        invoiceService.updateAppointmentPrice(appointment, setAppointmentPriceDto);
        invoiceService.saveInvoice(appointment.getInvoice());
        appointmentService.saveAppointment(appointment);
    }

    @Transactional
    public void payForAppointment(Long id, PayForAppointmentDto payForAppointmentDto) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        invoiceService.payInvoice(appointment.getInvoice().getInvoiceId(), payForAppointmentDto);
        appointmentService.saveAppointment(appointment);
    }
}
