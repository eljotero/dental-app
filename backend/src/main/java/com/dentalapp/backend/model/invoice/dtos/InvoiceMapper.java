package com.dentalapp.backend.model.invoice.dtos;

import com.dentalapp.backend.model.enums.PaymentStatus;
import com.dentalapp.backend.model.enums.PaymentType;
import com.dentalapp.backend.model.invoice.entity.Invoice;

import java.time.LocalDate;

public class InvoiceMapper {

    public static Invoice toDto(Invoice invoice, PayForAppointmentDto payForAppointmentDto) {
        invoice.setPaymentType(PaymentType.valueOf(payForAppointmentDto.getPaymentType()));
        invoice.setPaymentDate(LocalDate.parse(payForAppointmentDto.getPaymentDate()));
        invoice.setPaymentStatus(PaymentStatus.PAID);
        invoice.setIsPaid(true);
        invoice.setPrice(payForAppointmentDto.getPrice());
        return invoice;
    }
}
