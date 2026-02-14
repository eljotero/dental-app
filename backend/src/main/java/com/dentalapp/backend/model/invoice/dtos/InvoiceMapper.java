package com.dentalapp.backend.model.invoice.dtos;

import com.dentalapp.backend.model.enums.PaymentMethod;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {PaymentMethod.class, LocalDate.class})
public interface InvoiceMapper {

    @Mapping(target = "paymentMethod", expression = "java(PaymentMethod.valueOf(payForAppointmentDto.getPaymentType()))")
    @Mapping(target = "paymentDate", expression = "java(LocalDate.parse(payForAppointmentDto.getPaymentDate()))")
    @Mapping(target = "paymentStatus", constant = "PAID")
    @Mapping(target = "price", expression = "java(isPriceSet ? invoice.getPrice() : payForAppointmentDto.getPrice())")
    @Mapping(target = "isPaid", constant = "true")
    Invoice toDto(@MappingTarget Invoice invoice, PayForAppointmentDto payForAppointmentDto, boolean isPriceSet);
}