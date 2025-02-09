package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.file.dtos.GetDownloadFileDto;
import com.dentalapp.backend.model.file.dtos.GetFileDto;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.FileService;
import com.dentalapp.backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;
    private final AppointmentService appointmentService;
    private final UserService userService;

    public FileController(FileService fileService, AppointmentService appointmentService, UserService userService) {
        this.fileService = fileService;
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @fileService.isUsersFile(#id, authentication.principal.username)")
    public ResponseEntity<GetDownloadFileDto> getFileById(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getFileById(id));
    }

    @GetMapping("/appointment/{id}")
    @PreAuthorize("isAuthenticated() and @appointmentService.isUsersAppointment(#id, authentication.principal.username)")
    public ResponseEntity<List<GetFileDto>> getFilesByAppointmentId(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(fileService.getFilesByAppointmentId(appointment));
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("isAuthenticated() and @appointmentService.isUsersAppointment(#id, authentication.principal.username)")
    public ResponseEntity<List<GetFileDto>> getPatientFiles(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(fileService.getFilesByUserId(user));
    }

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated() and @appointmentService.isUsersAppointment(#id, authentication.principal.username)")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("id") Long id) throws IOException {
        appointmentService.uploadFileToAppointment(id, multipartFile);
        return ResponseEntity.status(201).body("File uploaded successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @appointmentService.isUsersAppointment(#id, authentication.principal.username)")
    public ResponseEntity<String> updateFile(@PathVariable Long id, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        System.out.println(multipartFile);
        fileService.updateFile(id, multipartFile);
        return ResponseEntity.status(200).body("File updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @fileService.isUsersFile(#id, authentication.principal.username)")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.status(200).body("File deleted successfully");
    }
}
