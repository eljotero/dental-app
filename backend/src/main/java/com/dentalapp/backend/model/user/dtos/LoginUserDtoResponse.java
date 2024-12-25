package com.dentalapp.backend.model.user.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDtoResponse {

    private String token;

    private String role;

    private String language;
}
