package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.controllers.PaymentController;
import com.dentalapp.backend.model.invoice.dtos.PayForAppointmentDto;
import com.dentalapp.backend.model.invoice.dtos.SetAppointmentPriceDto;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.InvoiceService;
import com.dentalapp.backend.services.PaymentService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTests {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private SetAppointmentPriceDto setAppointmentPriceDto;

    private PayForAppointmentDto payForAppointmentDto;

    private Validator validator;

    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        setAppointmentPriceDto = new SetAppointmentPriceDto();
        setAppointmentPriceDto.setPrice(100L);

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        payForAppointmentDto = new PayForAppointmentDto();
        payForAppointmentDto.setPrice(-5L);
        payForAppointmentDto.setPaymentType("");
        payForAppointmentDto.setPaymentDate("");

        invoice = new Invoice();
    }

    @Test
    public void testValidation() {
        setAppointmentPriceDto.setPrice(-100L);
        Assertions.assertEquals(1, validator.validate(setAppointmentPriceDto).size());
        Assertions.assertEquals(3, validator.validate(payForAppointmentDto).size());
        payForAppointmentDto.setPaymentDate("123123123123");
        Assertions.assertEquals(3, validator.validate(payForAppointmentDto).size());
    }

    @Test
    public void testGetAllInvoices() {
        String status = "PAID";
        String method = "CREDIT_CARD";
        LocalDate date = LocalDate.of(2021, 10, 10);
        List<Invoice> invoiceList = List.of(new Invoice());

        Specification<Invoice> specification = Specification.where(null);
        specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentStatus"), status))
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentMethod"), method))
                .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paymentDate"), date));

        when(invoiceService.findAllByQueryParams(any(Specification.class))).thenReturn(invoiceList);

        ResponseEntity<List<Invoice>> response = paymentController.getAllInvoices(status, method, date);

        verify(invoiceService, times(1)).findAllByQueryParams(any(Specification.class));
        Assertions.assertEquals(ResponseEntity.ok(invoiceList), response);
    }

    @Test
    public void testGetInvoiceById() {
        when(invoiceService.findById(1L)).thenReturn(invoice);
        ResponseEntity<Invoice> response = paymentController.getInvoice(1L);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals(invoice, response.getBody());
    }

    @Test
    public void testCreateInvoice() {
        ResponseEntity<String> response = paymentController.createInvoice(1L, setAppointmentPriceDto);
        verify(paymentService).setAppointmentPrice(1L, setAppointmentPriceDto);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Price set successfully", response.getBody());
    }

    @Test
    public void testUpdateInvoice() {
        ResponseEntity<String> response = paymentController.updateInvoice(1L, setAppointmentPriceDto);
        verify(paymentService).updateAppointmentPrice(1L, setAppointmentPriceDto);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Price updated successfully", response.getBody());
    }

    @Test
    public void testPayForAppointment() {
        payForAppointmentDto.setPaymentDate("2021-01-01");
        payForAppointmentDto.setPrice(100L);
        payForAppointmentDto.setPaymentType("CASH");
        ResponseEntity<String> response = paymentController.payInvoice(1L, payForAppointmentDto);
        verify(invoiceService).payInvoice(1L, payForAppointmentDto);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Appointment paid successfully", response.getBody());
    }
}
