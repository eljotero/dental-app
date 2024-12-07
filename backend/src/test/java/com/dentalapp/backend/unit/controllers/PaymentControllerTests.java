package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.controllers.PaymentController;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.invoice.dtos.PayForAppointmentDto;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.InvoiceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTests {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private PaymentController paymentController;

    private SetAppointmentPriceDto setAppointmentPriceDto;

    private Appointment appointment;

    @BeforeEach
    public void setUp() {
        setAppointmentPriceDto = new SetAppointmentPriceDto();
        setAppointmentPriceDto.setPrice(100L);

        appointment = new Appointment();
    }

    @Test
    public void testCreateInvoice() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointment);
        ResponseEntity<String> response = paymentController.createInvoice(1L, setAppointmentPriceDto);
        verify(invoiceService).setAppointmentPrice(appointment, setAppointmentPriceDto);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Price set successfully", response.getBody());
    }

    @Test
    public void testUpdateInvoice() {
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointment);
        ResponseEntity<String> response = paymentController.updateInvoice(1L, setAppointmentPriceDto);
        verify(invoiceService).updateAppointmentPrice(appointment, setAppointmentPriceDto);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Price updated successfully", response.getBody());
    }

    @Test
    public void testPayForAppointment() {
        PayForAppointmentDto payForAppointmentDto = new PayForAppointmentDto();
        payForAppointmentDto.setPaymentDate("2021-01-01");
        payForAppointmentDto.setPrice(100L);
        payForAppointmentDto.setPaymentDate("2021-01-01");
        ResponseEntity<String> response = paymentController.payInvoice(1L, payForAppointmentDto);
        verify(invoiceService).payInvoice(1L, payForAppointmentDto);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Appointment paid successfully", response.getBody());
    }
}
