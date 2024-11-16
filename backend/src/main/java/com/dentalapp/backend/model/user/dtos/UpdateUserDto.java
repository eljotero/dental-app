package com.dentalapp.backend.model.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    private String firstName;

    private String lastName;

    @Email(message = "Email is not valid")
    private String email;

    private String phoneNumber;

    private Boolean sex;

    private String personalIdNumber;

    private String country;

    private String city;

    private String addressLine;

    private String zipCode;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}
