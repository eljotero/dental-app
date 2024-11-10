package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.user.dtos.CreateUserDto;
import com.dentalapp.backend.model.user.dtos.LoginUserDto;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.model.user.exceptions.UserAlreadyExistsException;
import com.dentalapp.backend.model.user.repostitory.UserRepository;
import com.dentalapp.backend.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    private CreateUserDto createUserDto;

    private LoginUserDto loginUserDto;

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
        MockitoAnnotations.openMocks(this);

        loginUserDto = new LoginUserDto();
        loginUserDto.setEmail(createUserDto.getEmail());
        loginUserDto.setPassword(createUserDto.getPassword());
    }


    @Test
    public void testCreateUser() {
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(createUserDto.getPassword())).thenReturn("password");
        when(passwordEncoder.encode(createUserDto.getPersonalIdNumber())).thenReturn("123456789");
        userService.createUser(createUserDto);
    }

    @Test
    public void testCreateUserAlreadyExists() {
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(new User());
        when(passwordEncoder.encode(createUserDto.getPassword())).thenReturn("password");
        when(passwordEncoder.encode(createUserDto.getPersonalIdNumber())).thenReturn("123456789");
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(createUserDto));
    }

    @Test
    public void testLoginUser() {
        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setPassword("password");
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtService.generateToken(user)).thenReturn("token");
        String token = userService.loginUser(loginUserDto);
        Assertions.assertNotNull(token);
        Assertions.assertEquals("token", token);
    }

    @Test
    public void testLoginUserDoesNotExist() {
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(null);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.loginUser(loginUserDto));
    }
}