package com.dentalapp.backend.model.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    private String firstName;

    private String lastName;

    @Email(message = "Email is not valid")
    private String email;

    @Pattern(regexp = "^(\\+\\d{1,3})?(\\d{3})?\\d{3}\\d{3}$", message = "Phone number is not valid")
    private String phoneNumber;

    private Boolean sex;

    @Pattern(regexp = "^\\d{11}$", message = "Personal ID number is not valid")
    private String personalIdNumber;

    private String country;

    private String city;

    private String addressLine;

    @Pattern(regexp = "^\\d{5}$", message = "Zip code is not valid")
    private String zipCode;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}
