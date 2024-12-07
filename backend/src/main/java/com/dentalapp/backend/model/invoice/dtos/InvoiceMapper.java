package com.dentalapp.backend.model.invoice.dtos;

import com.dentalapp.backend.model.enums.PaymentStatus;
import com.dentalapp.backend.model.enums.PaymentType;
import com.dentalapp.backend.model.invoice.entity.Invoice;

import java.time.LocalDate;

public class InvoiceMapper {

    public static Invoice toDto(Invoice invoice, PayForAppointmentDto payForAppointmentDto, boolean isPriceSet) {
        invoice.setPaymentType(PaymentType.valueOf(payForAppointmentDto.getPaymentType()));
        invoice.setPaymentDate(LocalDate.parse(payForAppointmentDto.getPaymentDate()));
        invoice.setPaymentStatus(PaymentStatus.PAID);
        if(!isPriceSet) {
            invoice.setPrice(payForAppointmentDto.getPrice());
        }
        invoice.setIsPaid(true);
        return invoice;
    }
}
