package com.dentalapp.backend.services;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.user.dtos.*;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.model.user.exceptions.UserAlreadyExistsException;
import com.dentalapp.backend.model.user.exceptions.UserAuthenticationException;
import com.dentalapp.backend.model.user.exceptions.UserNotFoundException;
import com.dentalapp.backend.model.user.repostitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS)
public class UserService {

    @Value("${app.base-url}")
    private String baseUrl;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final ConfirmationTokenService confirmationTokenService;

    private final EmailSenderService emailSenderService;

    private final UserMapper userMapper;

    @Transactional
    public void createUser(CreateUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(createUserDto.getEmail());
        }
        createUserDto.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        createUserDto.setPersonalIdNumber(passwordEncoder.encode(createUserDto.getPersonalIdNumber()));
        User user = userMapper.toUser(createUserDto);
        userRepository.save(user);
        String tokenCode = confirmationTokenService.saveConfirmationToken(user);
        String link = baseUrl + "/api/user/confirm?token=" + tokenCode;
        String userName = user.getFirstName() + user.getLastName();
        emailSenderService.sendAccountConfirmationEmail(user.getEmail(), userName, link);
    }

    @Transactional
    public void enableUser(String token) {
        User user = userRepository.findById(confirmationTokenService.confirmToken(token)).orElseThrow(UserNotFoundException::new);
        user.setIsEnabled(true);
        userRepository.save(user);
    }

    public LoginUserDtoResponse loginUser(LoginUserDto loginUserDto) {
        User user = getUser(loginUserDto.getEmail());
        if (!user.isEnabled()) {
            throw UserAuthenticationException.accountDisabled(loginUserDto.getEmail());
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
        } catch (Exception e) {
            throw UserAuthenticationException.invalidCredentials();
        }
        String token = jwtService.generateToken(user);
        return userMapper.toResponse(user, token);
    }

    @Transactional
    public void updateUser(UpdateUserDto updateUserDto, String userEmail) {
        User user = getUser(userEmail);
        User updatedUserData = userMapper.toUpdateUser(user, updateUserDto);
        userRepository.save(updatedUserData);
    }

    public GetUserDto getUserData(String userEmail) {
        User user = getUser(userEmail);
        return userMapper.toGetUserDto(user);
    }

    public User getUser(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw UserNotFoundException.byEmail(userEmail);
        }
        return user.orElse(null);
    }

    public DoctorPageResponse<GetDoctorDto> getDoctors(UserQueryParams params) {
        return DoctorPageResponse.from(userRepository.findAllDoctors(params.toPageable()).map(userMapper::toGetDoctorDto));
    }

    public User getPatientById(Long patientId) {
        return userRepository.findPatientById(patientId).orElseThrow(UserNotFoundException::patientNotFound);
    }

    public User getDoctorById(Long doctorId) {
        return userRepository.findDoctorById(doctorId).orElseThrow(UserNotFoundException::doctorNotFound);
    }

    public User getPatientByEmail(String email) {
        return userRepository.findPatientByEmail(email).orElseThrow(UserNotFoundException::patientNotFound);
    }

    public User getDoctorByEmail(String email) {
        return userRepository.findDoctorByEmail(email).orElseThrow(UserNotFoundException::doctorNotFound);
    }

    public UserPageResponse<GetUserDto> getAllUsers(UserQueryParams params) {
        return UserPageResponse.from(userRepository.findAll(params.toPageable()).map(userMapper::toGetUserDto));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public GetUserDto getUserByIdDto(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.toGetUserDto(user);
    }

    @Transactional
    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        String tokenCode = confirmationTokenService.saveConfirmationToken(user);
        String link = baseUrl + "/api/user/reset-password?token=" + tokenCode;
        String userName = user.getFirstName() + user.getLastName();
        emailSenderService.sendResetPasswordEmail(user.getEmail(), userName, link);
    }

    @Transactional
    public void changePassword(String token, String password) {
        User user = userRepository.findById(confirmationTokenService.confirmToken(token)).orElseThrow(UserNotFoundException::new);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Transactional
    public void changeLanguage(String email, String language) {
        User user = getUser(email);
        user.setLanguage(language);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = getUser(email);
        userRepository.delete(user);
    }
}
