package com.dentalapp.backend.controllers;

import com.dentalapp.backend.configuration.JwtService;
import com.dentalapp.backend.model.user.dtos.CreateUserDto;
import com.dentalapp.backend.model.user.dtos.LoginUserDto;
import com.dentalapp.backend.model.user.dtos.UpdateUserDto;
import com.dentalapp.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid CreateUserDto createUserDto) {
        userService.createUser(createUserDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginUserDto loginUserDto) {
        String token = userService.loginUser(loginUserDto);
        return ResponseEntity.ok(token);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token, @RequestBody @Valid UpdateUserDto updateUserDto) {
        String email = jwtService.extractEmail(token.substring(7));
        userService.updateUser(updateUserDto, email);
        return ResponseEntity.ok("User updated successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmUser(@RequestParam String token) {
        userService.enableUser(token);
        return ResponseEntity.ok("User confirmed successfully");
    }
}
