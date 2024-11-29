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
import com.dentalapp.backend.services.ConfirmationTokenService;
import com.dentalapp.backend.services.EmailSenderService;
import com.dentalapp.backend.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private UserService userService;

    private CreateUserDto createUserDto;

    private LoginUserDto loginUserDto;

    private UpdateUserDto updateUserDto;

    private final String token = "token";

    private final String getEmail = "test@mail.com";

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
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(createUserDto.getPassword())).thenReturn("password");
        when(passwordEncoder.encode(createUserDto.getPersonalIdNumber())).thenReturn("123456789");
        when(confirmationTokenService.saveConfirmationToken(any())).thenReturn("token");
        userService.createUser(createUserDto);
    }

    @Test
    public void testEnableUser() {
        when(confirmationTokenService.confirmToken(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(new User()));
        userService.enableUser(token);
    }

    @Test
    public void testEnableUserNotFound() {
        when(confirmationTokenService.confirmToken(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.enableUser(token));
    }

    @Test
    public void testCreateUserAlreadyExists() {
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(Optional.of(new User()));
        when(passwordEncoder.encode(createUserDto.getPassword())).thenReturn("password");
        when(passwordEncoder.encode(createUserDto.getPersonalIdNumber())).thenReturn("123456789");
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(createUserDto));
    }

    @Test
    public void testLoginUser() {
        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setPassword("password");
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtService.generateToken(user)).thenReturn(token);
        String dbToken = userService.loginUser(loginUserDto);
        Assertions.assertNotNull(dbToken);
        Assertions.assertEquals(token, dbToken);
    }

    @Test
    public void testLoginUserDoesNotExist() {
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.loginUser(loginUserDto));
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        when(userRepository.findByEmail(getEmail)).thenReturn(Optional.of(user));
        userService.updateUser(updateUserDto, getEmail);
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findByEmail(getEmail)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(updateUserDto, getEmail));
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
        User user = new User();
        user.setUserType(UserType.PATIENT);
        when(userRepository.findPatientByEmail(getEmail)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getPatientByEmail(getEmail));
    }

    @Test
    public void testGetPatientByEmailNotFound() {
        when(userRepository.findPatientByEmail(getEmail)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getPatientByEmail(getEmail));
    }

    @Test
    public void testGetDoctorByEmail() {
        User user = new User();
        user.setUserType(UserType.DOCTOR);
        when(userRepository.findDoctorByEmail(getEmail)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getDoctorByEmail(getEmail));
    }

    @Test
    public void testGetDoctorByEmailNotFound() {
        when(userRepository.findDoctorByEmail(getEmail)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getDoctorByEmail(getEmail));
    }

    @Test
    public void testGetUsers() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(List.of(user));
        Assertions.assertEquals(List.of(user), userService.getAllUsers());
    }

    @Test
    public void testGetUserById() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getUserById(userId));
    }

    @Test
    public void testGetUserByIdNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }
}