package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.controllers.UserController;
import com.dentalapp.backend.model.enums.UserType;
import com.dentalapp.backend.model.user.dtos.*;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.UserService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserController userController;

    private CreateUserDto createUserDto;

    private LoginUserDto loginUserDto;

    private UpdateUserDto updateUserDto;

    private Validator validator;

    private User user;


    @BeforeEach
    public void setUp() {
        createUserDto = new CreateUserDto();
        createUserDto.setFirstName("John");
        createUserDto.setLastName("Doe");
        createUserDto.setEmail("test@mail.com");
        createUserDto.setPassword("password");
        createUserDto.setPhoneNumber("123456789");
        createUserDto.setSex(true);
        createUserDto.setPersonalIdNumber("123456789");
        createUserDto.setCountry("Country");
        createUserDto.setCity("City");
        createUserDto.setAddressLine("Address");
        createUserDto.setZipCode("12345");
        createUserDto.setDateOfBirth(LocalDate.of(1990, 1, 1));

        loginUserDto = new LoginUserDto();
        loginUserDto.setEmail(createUserDto.getEmail());
        loginUserDto.setPassword(createUserDto.getPassword());

        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("John");
        updateUserDto.setLastName("Doe");
        updateUserDto.setEmail("test@mail.com");
        updateUserDto.setPhoneNumber("123456789");
        updateUserDto.setSex(true);
        updateUserDto.setPersonalIdNumber("123456789");
        updateUserDto.setCountry("Country");
        updateUserDto.setCity("City");
        updateUserDto.setAddressLine("Address");
        updateUserDto.setZipCode("12345");
        updateUserDto.setDateOfBirth(LocalDate.of(1990, 1, 1));

        user = new User();
        user.setUserId(1L);
        user.setLanguage("en");
        user.setUserType(UserType.valueOf("PATIENT"));
    }

    @Test
    public void testRegisterUser() {
        doNothing().when(userService).createUser(createUserDto);
        ResponseEntity<?> response = userController.registerUser(createUserDto);
        verify(userService).createUser(createUserDto);
        assertEquals(ResponseEntity.ok("User registered successfully"), response);
    }

    @Test
    public void testValidation() {
        createUserDto.setFirstName("");
        createUserDto.setLastName("");
        createUserDto.setEmail("");
        createUserDto.setPassword("");
        createUserDto.setPhoneNumber("");
        createUserDto.setPersonalIdNumber("");
        createUserDto.setCountry("");
        createUserDto.setCity("");
        createUserDto.setAddressLine("");
        createUserDto.setZipCode("");
        createUserDto.setDateOfBirth(LocalDate.now().plusDays(1));
        assertEquals(16, validator.validate(createUserDto).size());
        createUserDto.setEmail("test");
        assertEquals(16, validator.validate(createUserDto).size());
        createUserDto.setPassword("pass");
        assertEquals(15, validator.validate(createUserDto).size());
        createUserDto.setPhoneNumber("123");
        assertEquals(14, validator.validate(createUserDto).size());
        createUserDto.setPersonalIdNumber("123");
        assertEquals(13, validator.validate(createUserDto).size());
        createUserDto.setZipCode("123");
        assertEquals(12, validator.validate(createUserDto).size());
    }

    @Test
    public void testLogin() {
        LoginUserDtoResponse loginUserDtoResponse = new LoginUserDtoResponse();
        loginUserDtoResponse.setLanguage("en");
        loginUserDtoResponse.setRole("PATIENT");
        when(userService.loginUser(loginUserDto)).thenReturn(loginUserDtoResponse);
        when(userService.getUser(loginUserDto.getEmail())).thenReturn(user);
        when(userService.loginUser(loginUserDto)).thenReturn(loginUserDtoResponse);
        userController.loginUser(loginUserDto);
        verify(userService).loginUser(loginUserDto);
    }

    @Test
    public void testUpdateUser() {
        String token = "Bearer test@mail.com";
        String email = token.substring(7);
        when((jwtService).extractEmail(token)).thenReturn(email);
        doNothing().when(userService).updateUser(updateUserDto, email);
        ResponseEntity<?> response = userController.updateUser(token, updateUserDto);
        assertEquals(ResponseEntity.ok("User updated successfully"), response);
        Assertions.assertEquals(ResponseEntity.ok("User updated successfully"), response);
    }

    @Test
    public void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(user));
        ResponseEntity<?> response = userController.getAllUsers();
        verify(userService).getAllUsers();
        Assertions.assertEquals(ResponseEntity.ok(List.of(user)), response);
    }

    @Test
    public void testGetUserById() {
        when(userService.getUserById(1L)).thenReturn(user);
        ResponseEntity<?> response = userController.getUserById(1L);
        verify(userService).getUserById(1L);
        Assertions.assertEquals(ResponseEntity.ok(user), response);
    }

    @Test
    public void testConfirmUser() {
        doNothing().when(userService).enableUser("token");
        ResponseEntity<?> response = userController.confirmUser("token");
        verify(userService).enableUser("token");
        assertEquals(ResponseEntity.ok("User confirmed successfully"), response);
    }

    @Test
    public void testResetPassword() {
        ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
        resetPasswordDto.setEmail("test@mail.com");
        doNothing().when(userService).resetPassword(resetPasswordDto.getEmail());
        ResponseEntity<?> response = userController.resetPassword(resetPasswordDto);
        verify(userService).resetPassword(resetPasswordDto.getEmail());
        assertEquals(ResponseEntity.ok("Password reset link sent to your email"), response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testChangePassword() {
        UpdatePasswordDto updatePasswordDto = new UpdatePasswordDto();
        updatePasswordDto.setPassword("password");
        doNothing().when(userService).changePassword("token", updatePasswordDto.getPassword());
        ResponseEntity<?> response = userController.changePassword("token", updatePasswordDto);
        verify(userService).changePassword("token", updatePasswordDto.getPassword());
        assertEquals(ResponseEntity.ok("Password changed successfully"), response);
        assertEquals(200, response.getStatusCode().value());
    }
}
