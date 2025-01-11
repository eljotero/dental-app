package com.dentalapp.backend.model.user.dtos;

import com.dentalapp.backend.model.user.entity.User;

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
        user.setLanguage(createUserDto.getLanguage());
        return user;
    }

    public static User toUpdateUser(User user, UpdateUserDto updateUserDto) {
        if(updateUserDto.getFirstName() != null) {
            user.setFirstName(updateUserDto.getFirstName());
        }
        if(updateUserDto.getLastName() != null) {
            user.setLastName(updateUserDto.getLastName());
        }
        if(updateUserDto.getEmail() != null) {
            user.setEmail(updateUserDto.getEmail());
        }
        if(updateUserDto.getPhoneNumber() != null) {
            user.setPhoneNumber(updateUserDto.getPhoneNumber());
        }
        if(updateUserDto.getSex() != null) {
            user.setSex(updateUserDto.getSex());
        }
        if(updateUserDto.getPersonalIdNumber() != null) {
            user.setPersonalId(updateUserDto.getPersonalIdNumber());
        }
        if(updateUserDto.getCountry() != null) {
            user.setCountry(updateUserDto.getCountry());
        }
        if(updateUserDto.getCity() != null) {
            user.setCity(updateUserDto.getCity());
        }
        if(updateUserDto.getAddressLine() != null) {
            user.setAddress(updateUserDto.getAddressLine());
        }
        if(updateUserDto.getZipCode() != null) {
            user.setZipCode(updateUserDto.getZipCode());
        }
        if(updateUserDto.getDateOfBirth() != null) {
            user.setDateOfBirth(updateUserDto.getDateOfBirth());
        }
        return user;
    }

    public static LoginUserDtoResponse toResponse(User user, String token) {
        LoginUserDtoResponse loginUserDtoResponse = new LoginUserDtoResponse();
        loginUserDtoResponse.setToken(token);
        loginUserDtoResponse.setRole(user.getUserType().name());
        loginUserDtoResponse.setLanguage(user.getLanguage());
        return loginUserDtoResponse;
    }

    public static GetDoctorDto toGetDoctorDto(User user) {
        GetDoctorDto getDoctorDto = new GetDoctorDto();
        getDoctorDto.setDoctorId(user.getUserId());
        getDoctorDto.setFirstName(user.getFirstName());
        getDoctorDto.setLastName(user.getLastName());
        return getDoctorDto;
    }
}
