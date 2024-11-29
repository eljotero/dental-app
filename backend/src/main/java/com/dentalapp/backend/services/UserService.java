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
        emailSenderService.sendConfirmationEmail(user.getEmail(), buildEmail(user.getFirstName(), link));
    }

    @Transactional
    public void enableUser(String token) {
        User user = userRepository.findById(confirmationTokenService.confirmToken(token)).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setIsEnabled(true);
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
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email " + userEmail + " does not exist");
        }
        return user.orElse(null);
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

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
