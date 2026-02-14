package com.dentalapp.backend.model.user.dtos;

import com.dentalapp.backend.model.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "address", source = "addressLine")
    GetUserDto toGetUserDto(User user);

    @Mapping(target = "language", ignore = true)
    User toUser(CreateUserDto user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "isEnabled", ignore = true)
    @Mapping(target = "isAccountNonExpired", ignore = true)
    @Mapping(target = "isAccountNonLocked", ignore = true)
    @Mapping(target = "isCredentialsNonExpired", ignore = true)
    @Mapping(target = "language", ignore = true)
    User toUpdateUser(@MappingTarget User user, UpdateUserDto updateUserDto);

    @Mapping(target = "role", expression = "java(user.getUserType().name())")
    @Mapping(target = "language", source = "user.language")
    @Mapping(target = "token", source = "token")
    LoginUserDtoResponse toResponse(User user, String token);

    @Mapping(target = "doctorId", source = "userId")
    GetDoctorDto toGetDoctorDto(User user);
}
