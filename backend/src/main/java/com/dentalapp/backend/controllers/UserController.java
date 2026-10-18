package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.user.dtos.ChangeLanguageDto;
import com.dentalapp.backend.model.user.dtos.CreateUserDto;
import com.dentalapp.backend.model.user.dtos.DoctorPageResponse;
import com.dentalapp.backend.model.user.dtos.GetDoctorDto;
import com.dentalapp.backend.model.user.dtos.GetUserDto;
import com.dentalapp.backend.model.user.dtos.LoginUserDto;
import com.dentalapp.backend.model.user.dtos.LoginUserDtoResponse;
import com.dentalapp.backend.model.user.dtos.ResetPasswordDto;
import com.dentalapp.backend.model.user.dtos.UpdatePasswordDto;
import com.dentalapp.backend.model.user.dtos.UpdateUserDto;
import com.dentalapp.backend.model.user.dtos.UserPageResponse;
import com.dentalapp.backend.model.user.dtos.UserQueryParams;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

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
    public ResponseEntity<String> updateUser(@AuthenticationPrincipal User user, @RequestBody @Valid UpdateUserDto updateUserDto) {
        userService.updateUser(updateUserDto, user.getEmail());
        return ResponseEntity.ok("User updated successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<UserPageResponse<GetUserDto>> getAllUsers(@Valid @ModelAttribute UserQueryParams params) {
        return ResponseEntity.ok(userService.getAllUsers(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserByIdDto(id));
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserDto> getMe(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserData(user.getEmail()));
    }

    @GetMapping("/doctors")
    public ResponseEntity<DoctorPageResponse<GetDoctorDto>> getDoctors(@Valid @ModelAttribute UserQueryParams params) {
        return ResponseEntity.ok(userService.getDoctors(params));
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
    public ResponseEntity<String> changeLanguage(@AuthenticationPrincipal User user, @RequestBody @Valid ChangeLanguageDto changeLanguageDto) {
        userService.changeLanguage(user.getEmail(), changeLanguageDto.getLanguage());
        return ResponseEntity.ok("Language changed successfully");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal User user) {
        userService.deleteUser(user.getEmail());
        return ResponseEntity.ok("User deleted successfully");
    }
}
