package com.dentalapp.backend.model.user.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetDoctorDto {
    private Long doctorId;
    private String firstName;
    private String lastName;
}
