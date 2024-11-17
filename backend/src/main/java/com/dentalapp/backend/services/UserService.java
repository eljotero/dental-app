package com.dentalapp.backend.services;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.enums.UserType;
import com.dentalapp.backend.model.user.dtos.CreateUserDto;
import com.dentalapp.backend.model.user.dtos.LoginUserDto;
import com.dentalapp.backend.model.user.dtos.UpdateUserDto;
import com.dentalapp.backend.model.user.dtos.UserMapper;
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

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public void createUser(CreateUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()) != null) {
            throw new UserAlreadyExistsException("User with email " + createUserDto.getEmail() + " already exists");
        }
        createUserDto.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        createUserDto.setPersonalIdNumber(passwordEncoder.encode(createUserDto.getPersonalIdNumber()));
        User user = UserMapper.toUser(createUserDto);
        user.setUserType(UserType.PATIENT);
        userRepository.save(user);
    }

    public String loginUser(LoginUserDto loginUserDto) {
        User user = getUser(loginUserDto.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
        return jwtService.generateToken(user);
    }

    @Transactional
    public void updateUser(UpdateUserDto updateUserDto, String userEmail) {
        User user = getUser(userEmail);
        User updatedUserData = UserMapper.toUpdateUser(user, updateUserDto);
        userRepository.save(updatedUserData);
    }

    public User getUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException("User with email " + userEmail + " does not exist");
        }
        return user;
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
}
