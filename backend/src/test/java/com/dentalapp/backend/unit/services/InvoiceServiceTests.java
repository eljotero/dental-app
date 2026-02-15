package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.invoice.dtos.InvoiceMapper;
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
import org.springframework.data.jpa.domain.Specification;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTests {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    @InjectMocks
    private InvoiceService invoiceService;

    private Invoice invoice;

    private Appointment appointment;

    private SetAppointmentPriceDto setAppointmentPriceDto;

    private PayForAppointmentDto payForAppointmentDto;

    @BeforeEach
    void setUp() {
        invoice = new Invoice();

        setAppointmentPriceDto = new SetAppointmentPriceDto();
        setAppointmentPriceDto.setPrice(100L);

        appointment = new Appointment();
        appointment.setInvoice(invoice);

        payForAppointmentDto = new PayForAppointmentDto();
        payForAppointmentDto.setPaymentDate("2021-01-01");
        payForAppointmentDto.setPrice(100L);
        payForAppointmentDto.setPaymentType("CASH");
    }

    @Test
    void testFindAll() {
        when(invoiceRepository.findAll((Specification<Invoice>) any())).thenReturn(java.util.List.of(invoice));
        Assertions.assertEquals(1, invoiceService.findAllByQueryParams(null).size());
    }

    @Test
    void testFindById() {
        when(invoiceRepository.findById(1L)).thenReturn(java.util.Optional.of(invoice));
        Assertions.assertEquals(invoice, invoiceService.findById(1L));
    }

    @Test
    void testFindByIdNotFound() {
        when(invoiceRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(com.dentalapp.backend.model.invoice.exceptions.InvoiceNotFoundException.class, () ->
                invoiceService.findById(1L)
        );
    }

    @Test
    void testCreateInvoice() {
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        Invoice createdInvoice = invoiceService.createInvoice();
        Assertions.assertEquals(invoice.getPrice(), createdInvoice.getPrice());
        Assertions.assertEquals(invoice.getIsPaid(), createdInvoice.getIsPaid());
        Assertions.assertEquals(invoice.getPaymentMethod(), createdInvoice.getPaymentMethod());
        Assertions.assertEquals(invoice.getPaymentDate(), createdInvoice.getPaymentDate());
        Assertions.assertEquals(invoice.getPaymentStatus(), createdInvoice.getPaymentStatus());
    }

    @Test
    void testUpdateAppointmentPrice() {
        Long expectedPrice = 100L;
        setAppointmentPriceDto.setPrice(expectedPrice);

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        invoiceService.updateAppointmentPrice(appointment, setAppointmentPriceDto);

        verify(invoiceRepository).save(any(Invoice.class));
        Assertions.assertEquals(expectedPrice, appointment.getInvoice().getPrice());
    }


    @Test
    void testPayInvoice() {
        Invoice updatedInvoice = new Invoice();

        when(invoiceRepository.findById(1L)).thenReturn(java.util.Optional.of(invoice));
        when(invoiceMapper.toDto(eq(invoice), eq(payForAppointmentDto), anyBoolean())).thenReturn(updatedInvoice);
        when(invoiceRepository.save(updatedInvoice)).thenReturn(updatedInvoice);

        invoiceService.payInvoice(1L, payForAppointmentDto);

        verify(invoiceMapper).toDto(eq(invoice), eq(payForAppointmentDto), anyBoolean());
        verify(invoiceRepository).save(updatedInvoice);
    }


    @Test
    void testPayInvoiceNotFound() {
        when(invoiceRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(com.dentalapp.backend.model.invoice.exceptions.InvoiceNotFoundException.class, () ->
                invoiceService.payInvoice(1L, payForAppointmentDto)
        );
    }

    @Test
    void testSaveInvoice() {
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        invoiceService.saveInvoice(invoice);
        verify(invoiceRepository).save(invoice);
    }

}
