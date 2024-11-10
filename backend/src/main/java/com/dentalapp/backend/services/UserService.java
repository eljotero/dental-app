package com.dentalapp.backend.services;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.enums.UserType;
import com.dentalapp.backend.model.user.dtos.CreateUserDto;
import com.dentalapp.backend.model.user.dtos.LoginUserDto;
import com.dentalapp.backend.model.user.dtos.UserMapper;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.model.user.exceptions.UserAlreadyExistsException;
import com.dentalapp.backend.model.user.repostitory.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User user = userRepository.findByEmail(loginUserDto.getEmail());
        if (user == null) {
            throw new UserAlreadyExistsException("User with email " + loginUserDto.getEmail() + " does not exist");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
        return jwtService.generateToken(user);
    }
}
