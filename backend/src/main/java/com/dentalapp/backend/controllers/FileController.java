package com.dentalapp.backend.controllers;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.file.dtos.GetDownloadFileDto;
import com.dentalapp.backend.model.file.dtos.GetFileDto;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.FileService;
import com.dentalapp.backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    private final AppointmentService appointmentService;

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<GetDownloadFileDto> getFileById(@PathVariable Long id) {
        return ResponseEntity.ok(fileService.getFileById(id));
    }

    @GetMapping("/appointment/{id}")
    public ResponseEntity<List<GetFileDto>> getFilesByAppointmentId(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(fileService.getFilesByAppointmentId(appointment));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<GetFileDto>> getPatientFiles(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(fileService.getFilesByUserId(user));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("id") Long id) {
        appointmentService.uploadFileToAppointment(id, multipartFile);
        return ResponseEntity.status(201).body("File uploaded successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFile(@PathVariable Long id, @RequestParam("file") MultipartFile multipartFile) {
        fileService.updateFile(id, multipartFile);
        return ResponseEntity.status(200).body("File updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.status(200).body("File deleted successfully");
    }
}
