package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.enums.UserType;
import com.dentalapp.backend.model.user.dtos.*;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTests {

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

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private CreateUserDto createUserDto;

    private LoginUserDto loginUserDto;

    private UpdateUserDto updateUserDto;

    private final String token = "token";

    private final String getEmail = "test@mail.com";

    private User user;

    @BeforeEach
    void setUp() {
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

        user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setPassword("password");
        user.setEmail("test@mail.com");
        user.setUserType(UserType.PATIENT);
        user.setIsEnabled(true);
    }


    @Test
    void testCreateUser() {
        User newUser = new User();
        newUser.setFirstName("John");
        newUser.setEmail(createUserDto.getEmail());
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toUser(createUserDto)).thenReturn(newUser);
        when(passwordEncoder.encode(createUserDto.getPassword())).thenReturn("password");
        when(passwordEncoder.encode(createUserDto.getPersonalIdNumber())).thenReturn("123456789");
        when(confirmationTokenService.saveConfirmationToken(any())).thenReturn("token");
        userService.createUser(createUserDto);
        verify(userRepository).save(any(User.class));
    }


    @Test
    void testEnableUser() {
        when(confirmationTokenService.confirmToken(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        userService.enableUser(token);
        verify(confirmationTokenService).confirmToken(token);
        verify(userRepository).findById(1L);
        Assertions.assertTrue(user.isEnabled());
    }


    @Test
    void testEnableUserNotFound() {
        when(confirmationTokenService.confirmToken(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.enableUser(token));
    }

    @Test
    void testCreateUserAlreadyExists() {
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(createUserDto.getPassword())).thenReturn("password");
        when(passwordEncoder.encode(createUserDto.getPersonalIdNumber())).thenReturn("123456789");
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(createUserDto));
    }


    @Test
    void testLoginUserDoesNotExist() {
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.loginUser(loginUserDto));
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setEmail(getEmail);
        when(userRepository.findByEmail(getEmail)).thenReturn(Optional.of(user));
        when(userMapper.toUpdateUser(user, updateUserDto)).thenReturn(updatedUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        userService.updateUser(updateUserDto, getEmail);
        verify(userRepository).findByEmail(getEmail);
        verify(userMapper).toUpdateUser(user, updateUserDto);
        verify(userRepository).save(updatedUser);
    }


    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findByEmail(getEmail)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(updateUserDto, getEmail));
    }

    @Test
    void testGetPatientById() {
        Long patientId = 1L;
        user.setUserType(UserType.PATIENT);
        when(userRepository.findPatientById(patientId)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getPatientById(patientId));
    }

    @Test
    void testGetPatientByIdNotFound() {
        Long patientId = 1L;
        when(userRepository.findPatientById(patientId)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getPatientById(patientId));
    }

    @Test
    void testGetDoctorById() {
        Long doctorId = 1L;
        user.setUserType(UserType.DOCTOR);
        when(userRepository.findDoctorById(doctorId)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getDoctorById(doctorId));
    }

    @Test
    void testGetDoctorByIdNotFound() {
        Long doctorId = 1L;
        when(userRepository.findDoctorById(doctorId)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getDoctorById(doctorId));
    }

    @Test
    void testGetPatientByEmail() {
        user.setUserType(UserType.PATIENT);
        when(userRepository.findPatientByEmail(getEmail)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getPatientByEmail(getEmail));
    }

    @Test
    void testGetPatientByEmailNotFound() {
        when(userRepository.findPatientByEmail(getEmail)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getPatientByEmail(getEmail));
    }

    @Test
    void testGetDoctorByEmail() {
        user.setUserType(UserType.DOCTOR);
        when(userRepository.findDoctorByEmail(getEmail)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getDoctorByEmail(getEmail));
    }

    @Test
    void testGetDoctorByEmailNotFound() {
        when(userRepository.findDoctorByEmail(getEmail)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getDoctorByEmail(getEmail));
    }

    @Test
    void testGetUsers() {
        GetUserDto expectedDto = new GetUserDto();
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toGetUserDto(user)).thenReturn(expectedDto);

        List<GetUserDto> result = userService.getAllUsers();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        verify(userRepository).findAll();
        verify(userMapper).toGetUserDto(user);
    }


    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        Assertions.assertEquals(user, userService.getUserById(1L));
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testResetPassword() {
        user.setEmail(createUserDto.getEmail());
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(Optional.of(user));
        when(confirmationTokenService.saveConfirmationToken(any())).thenReturn("token");
        userService.resetPassword(createUserDto.getEmail());
        verify(userRepository).findByEmail(createUserDto.getEmail());
        verify(confirmationTokenService).saveConfirmationToken(any());
        verify(emailSenderService).sendResetPasswordEmail(eq(createUserDto.getEmail()), anyString(), anyString());
    }

    @Test
    void testResetPasswordUserNotFound() {
        String email = createUserDto.getEmail();
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.resetPassword(email);
        });
    }

    @Test
    void testChangePassword() {
        String newPassword = createUserDto.getPassword();
        when(confirmationTokenService.confirmToken(token)).thenReturn(user.getUserId());
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        userService.changePassword(token, newPassword);
        verify(confirmationTokenService).confirmToken(token);
        verify(userRepository).findById(user.getUserId());
        verify(userRepository).save(user);
    }


    @Test
    void testChangePasswordUserNotFound() {
        String newPassword = createUserDto.getPassword();
        when(confirmationTokenService.confirmToken(token)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.changePassword(token, newPassword);
        });
    }

}