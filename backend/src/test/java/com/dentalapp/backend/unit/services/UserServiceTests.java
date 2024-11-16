package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.enums.UserType;
import com.dentalapp.backend.model.user.dtos.CreateUserDto;
import com.dentalapp.backend.model.user.dtos.LoginUserDto;
import com.dentalapp.backend.model.user.dtos.UpdateUserDto;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.model.user.exceptions.UserAlreadyExistsException;
import com.dentalapp.backend.model.user.exceptions.UserNotFoundException;
import com.dentalapp.backend.model.user.repostitory.UserRepository;
import com.dentalapp.backend.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    private UpdateUserDto updateUserDto;

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
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.loginUser(loginUserDto));
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        String userEmail = "test@mail.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        userService.updateUser(updateUserDto, userEmail);
    }

    @Test
    public void testUpdateUserNotFound() {
        String userEmail = "test@mail.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(null);
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(updateUserDto, userEmail));
    }

    @Test
    public void testGetPatientById() {
        Long patientId = 1L;
        User user = new User();
        user.setUserType(UserType.PATIENT);
        when(userRepository.findPatientById(patientId)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getPatientById(patientId));
    }

    @Test
    public void testGetPatientByIdNotFound() {
        Long patientId = 1L;
        when(userRepository.findPatientById(patientId)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getPatientById(patientId));
    }

    @Test
    public void testGetDoctorById() {
        Long doctorId = 1L;
        User user = new User();
        user.setUserType(UserType.DOCTOR);
        when(userRepository.findDoctorById(doctorId)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getDoctorById(doctorId));
    }

    @Test
    public void testGetDoctorByIdNotFound() {
        Long doctorId = 1L;
        when(userRepository.findDoctorById(doctorId)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getDoctorById(doctorId));
    }

    @Test
    public void testGetPatientByEmail() {
        String email = "test@mail.com";
        User user = new User();
        user.setUserType(UserType.PATIENT);
        when(userRepository.findPatientByEmail(email)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getPatientByEmail(email));
    }

    @Test
    public void testGetPatientByEmailNotFound() {
        String email = "test@mail.com";
        when(userRepository.findPatientByEmail(email)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getPatientByEmail(email));
    }

    @Test
    public void testGetDoctorByEmail() {
        String email = "test@mail.com";
        User user = new User();
        user.setUserType(UserType.DOCTOR);
        when(userRepository.findDoctorByEmail(email)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getDoctorByEmail(email));
    }

    @Test
    public void testGetDoctorByEmailNotFound() {
        String email = "test@mail.com";
        when(userRepository.findDoctorByEmail(email)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getDoctorByEmail(email));
    }
}