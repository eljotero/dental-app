package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.invoice.dtos.PayForAppointmentDto;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.model.invoice.repository.InvoiceRepository;
import com.dentalapp.backend.services.InvoiceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTests {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    private Invoice invoice;

    private Appointment appointment;

    private SetAppointmentPriceDto setAppointmentPriceDto;

    @BeforeEach
    public void setUp() {
        invoice = new Invoice();

        setAppointmentPriceDto = new SetAppointmentPriceDto();
        setAppointmentPriceDto.setPrice(100L);

        appointment = new Appointment();
        appointment.setInvoice(invoice);
    }

    @Test
    public void testCreateInvoice() {
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        Invoice createdInvoice = invoiceService.createInvoice();
        Assertions.assertEquals(invoice, createdInvoice);
    }

    @Test
    public void testSetAppointmentPrice() {
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        invoiceService.setAppointmentPrice(appointment, setAppointmentPriceDto);
    }

    @Test
    public void testUpdateAppointmentPrice() {
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        invoiceService.updateAppointmentPrice(appointment, setAppointmentPriceDto);
    }

    @Test
    public void testPayInvoice() {
        PayForAppointmentDto payForAppointmentDto = new PayForAppointmentDto();
        payForAppointmentDto.setPaymentDate("2021-01-01");
        payForAppointmentDto.setPrice(100L);
        payForAppointmentDto.setPaymentType("CASH");
        when(invoiceRepository.findById(1L)).thenReturn(java.util.Optional.of(invoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        invoiceService.payInvoice(1L, payForAppointmentDto);
    }
}
