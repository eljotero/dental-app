package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.InvoiceService;
import com.dentalapp.backend.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTests {
    @Mock
    private AppointmentService appointmentService;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private PaymentService paymentService;

    private SetAppointmentPriceDto setAppointmentPriceDto;

    private Appointment appointment;

    @BeforeEach
    public void setUp() {
        setAppointmentPriceDto = new SetAppointmentPriceDto();
        appointment = new Appointment();
    }

    @Test
    public void testSetAppointmentPrice() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointment);
        paymentService.setAppointmentPrice(1L, setAppointmentPriceDto);
        verify(invoiceService).setAppointmentPrice(appointment, setAppointmentPriceDto);
        verify(appointmentService).saveAppointment(appointment);
        verify(invoiceService).saveInvoice(appointment.getInvoice());
    }

    @Test
    public void testUpdateAppointmentPrice() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointment);
        paymentService.updateAppointmentPrice(1L, setAppointmentPriceDto);
        verify(invoiceService).updateAppointmentPrice(appointment, setAppointmentPriceDto);
        verify(appointmentService).saveAppointment(appointment);
        verify(invoiceService).saveInvoice(appointment.getInvoice());
    }
}
