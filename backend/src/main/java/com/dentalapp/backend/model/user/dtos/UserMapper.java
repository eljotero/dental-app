package com.dentalapp.backend.model.user.dtos;

import com.dentalapp.backend.model.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

public class UserMapper {

    public static User toUser(CreateUserDto createUserDto) {
        User user = new User();
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(createUserDto.getPassword());
        user.setPhoneNumber(createUserDto.getPhoneNumber());
        user.setSex(createUserDto.getSex());
        user.setPersonalId(createUserDto.getPersonalIdNumber());
        user.setCountry(createUserDto.getCountry());
        user.setCity(createUserDto.getCity());
        user.setAddress(createUserDto.getAddressLine());
        user.setZipCode(createUserDto.getZipCode());
        user.setDateOfBirth(createUserDto.getDateOfBirth());
        return user;
    }
}
