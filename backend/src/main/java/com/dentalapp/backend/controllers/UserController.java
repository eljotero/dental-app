package com.dentalapp.backend.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.user.dtos.*;
import com.dentalapp.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserDtoResponse> loginUser(@RequestBody @Valid LoginUserDto loginUserDto) {
        return ResponseEntity.ok(userService.loginUser(loginUserDto));
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestHeader("Authorization") String token, @RequestBody @Valid UpdateUserDto updateUserDto) {
        String email = jwtService.extractEmail(token.substring(7));
        userService.updateUser(updateUserDto, email);
        return ResponseEntity.ok("User updated successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<GetUserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserByIdDto(id));
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserDto> getMe(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getUserData(jwtService.extractEmail(token.substring(7))));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<GetDoctorDto>> getDoctors() {
        return ResponseEntity.ok(userService.getDoctors());
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam String token) {
        userService.enableUser(token);
        return ResponseEntity.ok("User confirmed successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto) {
        userService.resetPassword(resetPasswordDto.getEmail());
        return ResponseEntity.ok("Password reset link sent to your email");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String token, @RequestBody @Valid UpdatePasswordDto updatePasswordDto) {
        userService.changePassword(token, updatePasswordDto.getPassword());
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/change-language")
    public ResponseEntity<String> changeLanguage(@RequestHeader("Authorization") String token, @RequestBody @Valid ChangeLanguageDto changeLanguageDto) {
        String email = jwtService.extractEmail(token.substring(7));
        userService.changeLanguage(email, changeLanguageDto.getLanguage());
        return ResponseEntity.ok("Language changed successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String token) {
        String email = jwtService.extractEmail(token.substring(7));
        userService.deleteUser(email);
        return ResponseEntity.ok("User deleted successfully");
    }
}
