package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.controllers.UserController;
import com.dentalapp.backend.model.user.dtos.CreateUserDto;
import com.dentalapp.backend.model.user.dtos.LoginUserDto;
import com.dentalapp.backend.services.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private CreateUserDto createUserDto;

    private LoginUserDto loginUserDto;

    private Validator validator;


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

        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCreateUser() {
        doNothing().when(userService).createUser(createUserDto);
        ResponseEntity<?> response = userController.registerUser(createUserDto);
        verify(userService).createUser(createUserDto);
        assertEquals(ResponseEntity.ok().build(), response);
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
        Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(createUserDto);
        assertEquals(11, violations.size());
        createUserDto.setEmail("test");
        violations = validator.validate(createUserDto);
        assertEquals(11, violations.size());
    }

    @Test
    public void testLogin() {
        when(userService.loginUser(loginUserDto)).thenReturn("token");
        ResponseEntity<?> response = userController.loginUser(loginUserDto);
        verify(userService).loginUser(loginUserDto);
    }
}
