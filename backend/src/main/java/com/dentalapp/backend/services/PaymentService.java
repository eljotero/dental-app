package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {

    private final AppointmentService appointmentService;

    private final InvoiceService invoiceService;

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
}
