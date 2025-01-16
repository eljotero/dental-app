package com.dentalapp.backend.model.user.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeLanguageDto {

    @NotBlank(message = "Language is required")
    private String language;
}
