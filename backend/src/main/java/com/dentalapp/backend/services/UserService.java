package com.dentalapp.backend.services;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.enums.UserType;
import com.dentalapp.backend.model.user.dtos.*;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.model.user.exceptions.UserAlreadyExistsException;
import com.dentalapp.backend.model.user.exceptions.UserNotFoundException;
import com.dentalapp.backend.model.user.repostitory.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final ConfirmationTokenService confirmationTokenService;

    private final EmailSenderService emailSenderService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, ConfirmationTokenService confirmationTokenService, EmailSenderService emailSenderService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
    }

    @Transactional
    public void createUser(CreateUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + createUserDto.getEmail() + " already exists");
        }
        createUserDto.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        createUserDto.setPersonalIdNumber(passwordEncoder.encode(createUserDto.getPersonalIdNumber()));
        User user = UserMapper.toUser(createUserDto);
        user.setUserType(UserType.PATIENT);
        userRepository.save(user);
        String tokenCode = confirmationTokenService.saveConfirmationToken(user);
        String link = "http://localhost:8080/api/user/confirm?token=" + tokenCode;
        String userName = user.getFirstName() + user.getLastName();
        emailSenderService.sendAccountConfirmationEmail(user.getEmail(), userName, link);
    }

    @Transactional
    public void enableUser(String token) {
        User user = userRepository.findById(confirmationTokenService.confirmToken(token)).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setIsEnabled(true);
        userRepository.save(user);
    }

    public LoginUserDtoResponse loginUser(LoginUserDto loginUserDto) {
        User user = getUser(loginUserDto.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
        String token = jwtService.generateToken(user);
        return UserMapper.toResponse(user, token);
    }

    @Transactional
    public void updateUser(UpdateUserDto updateUserDto, String userEmail) {
        User user = getUser(userEmail);
        User updatedUserData = UserMapper.toUpdateUser(user, updateUserDto);
        userRepository.save(updatedUserData);
    }

    public User getUser(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email " + userEmail + " does not exist");
        }
        return user.orElse(null);
    }

    public List<GetDoctorDto> getDoctors() {
        return userRepository.findAllDoctors().stream().map(UserMapper::toGetDoctorDto).toList();
    }

    public User getPatientById(Long patientId) {
        return userRepository.findPatientById(patientId).orElseThrow(() -> new UserNotFoundException("Patient not found"));
    }

    public User getDoctorById(Long doctorId) {
        return userRepository.findDoctorById(doctorId).orElseThrow(() -> new UserNotFoundException("Doctor not found"));
    }

    public User getPatientByEmail(String email) {
        return userRepository.findPatientByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));
    }

    public User getDoctorByEmail(String email) {
        return userRepository.findDoctorByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Doctor not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        String tokenCode = confirmationTokenService.saveConfirmationToken(user);
        String link = "http://localhost:8080/api/user/reset-password?token=" + tokenCode;
        String userName = user.getFirstName() + user.getLastName();
        emailSenderService.sendResetPasswordEmail(user.getEmail(), userName, link);
    }

    @Transactional
    public void changePassword(String token, String password) {
        User user = userRepository.findById(confirmationTokenService.confirmToken(token)).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Transactional
    public void changeLanguage(String email, String language) {
        User user = getUser(email);
        user.setLanguage(language);
        userRepository.save(user);
    }
}
