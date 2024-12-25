package com.dentalapp.backend.model.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {

    @NotNull
    @Email
    private String email;
}
