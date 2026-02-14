package com.dentalapp.backend.model.user.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+\\d{1,3})?(\\d{3})?\\d{3}\\d{3}$", message = "Phone number is not valid")
    private String phoneNumber;

    @NotNull(message = "Sex is required")
    private Boolean sex;

    @NotBlank(message = "Personal ID number is required")
    @Pattern(regexp = "^\\d{11}$", message = "Personal ID number is not valid")
    private String personalIdNumber;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Address line is required")
    private String addressLine;

    @NotBlank(message = "Zip code is required")
    @Pattern(regexp = "^\\d{5}$", message = "Zip code is not valid")
    private String zipCode;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private String language;
}
