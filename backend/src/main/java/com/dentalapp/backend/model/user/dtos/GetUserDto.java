package com.dentalapp.backend.model.user.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Boolean sex;
    private String country;
    private String city;
    private String address;
    private String zipCode;
    private LocalDate dateOfBirth;
    private String language;
}