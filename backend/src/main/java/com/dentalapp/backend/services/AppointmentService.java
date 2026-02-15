package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.dtos.*;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.appointment.exceptions.AppointmentNotFoundException;
import com.dentalapp.backend.model.appointment.exceptions.IllegalAppointmentDate;
import com.dentalapp.backend.model.appointment.repository.AppointmentRepository;
import com.dentalapp.backend.model.file.entity.File;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionDto;
import com.dentalapp.backend.model.prescription.dtos.PrescriptionMapper;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import com.dentalapp.backend.model.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final UserService userService;

    private final AvailabilityService availabilityService;

    private final EmailSenderService emailSenderService;

    private final InvoiceService invoiceService;

    private final FileService fileService;

    private final AppointmentMapper appointmentMapper;

    private final PrescriptionMapper prescriptionMapper;

    public List<GetAppointmentDto> getAppointments(LocalDate date) {
        if (date == null) {
            return appointmentRepository.findAll().stream().map(appointmentMapper::toGetAppointmentDto).toList();
        }
        return appointmentRepository.findAllByDate(date).stream().map(appointmentMapper::toGetAppointmentDto).toList();
    }

    public List<GetAppointmentDto> getPatientAppointments(String patientEmail) {
        Long patientId = userService.getPatientByEmail(patientEmail).getUserId();
        return appointmentRepository.findAllByPatientId(patientId).stream().map(appointmentMapper::toGetAppointmentDto).toList();
    }

    public List<GetAppointmentDtoV3> getDoctorAppointments(String doctorEmail, LocalDate date) {
        Long doctorId = userService.getDoctorByEmail(doctorEmail).getUserId();
        if(date == null) {
            return appointmentRepository.findAllByDoctorId(doctorId).stream().map(appointmentMapper::toGetAppointmentDtoV3).toList();
        }
        return appointmentRepository.findAllByDoctorIdAndDate(doctorId, date).stream().map(appointmentMapper::toGetAppointmentDtoV3).toList();
    }

    public List<Appointment> getDoctorAppointmentsByDate(String doctorEmail, LocalDate date) {
        Long doctorId = userService.getDoctorByEmail(doctorEmail).getUserId();
        return appointmentRepository.findAllByDoctorIdAndDate(doctorId, date);
    }

    public List<Appointment> getDoctorsAppointmentsByDate(Long doctorId, LocalDate date) {
        return appointmentRepository.findAllByDoctorIdAndDate(doctorId, date);
    }

    @Transactional
    public GetAppointmentDtoV2 getAppointmentByIdDto(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        return appointmentMapper.toGetAppointmentDtoV2(appointment);
    }

    @Transactional
    public GetAppointmentDtoV4 getAppointmentByIdDtoV4(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
        return appointmentMapper.toGetAppointmentDtoV4(appointment);
    }

    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
    }

    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.findByDate(date);
    }

    @Transactional
    public void createAppointment(CreateAppointmentDto createAppointmentDto, String patientEmail) {
        User patient = userService.getPatientByEmail(patientEmail);
        User doctor = userService.getDoctorById(createAppointmentDto.getDoctorId());
        if (hasDoctorAppointmentAtTime(doctor, createAppointmentDto.getAppointmentDate(), LocalTime.parse(createAppointmentDto.getAppointmentStartTime()), LocalTime.parse(createAppointmentDto.getAppointmentEndTime()))) {
            throw new IllegalAppointmentDate("Doctor already has an appointment at this time");
        }
        if (!availabilityService.isDoctorAvailable(doctor, createAppointmentDto.getAppointmentDate(), LocalTime.parse(createAppointmentDto.getAppointmentStartTime()), LocalTime.parse(createAppointmentDto.getAppointmentEndTime()))) {
            throw new IllegalAppointmentDate("Doctor is not available at this time");
        }
        createAppointmentDto.setPatient(patient);
        createAppointmentDto.setDoctor(doctor);
        Appointment appointment = appointmentMapper.toAppointment(createAppointmentDto);
        Invoice invoice = invoiceService.createInvoice();
        appointment.setInvoice(invoice);
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void updateAppointment(UpdateAppointmentDto updateAppointmentDto, Long appointmentId, String email) {
        Appointment appointment = getAppointmentById(appointmentId);
        User doctor = userService.getDoctorByEmail(email);
        List<Appointment> appointments = appointmentRepository.findAllByDoctorAndDate(doctor, appointment.getAppointmentDate());
        appointments.stream().filter(a -> !Objects.equals(a.getAppointmentId(), appointment.getAppointmentId())).forEach(a -> {
            if (a.getAppointmentDate().equals(updateAppointmentDto.getAppointmentDate())) {
                throw new IllegalAppointmentDate("Doctor already has an appointment at this time");
            }
        });
        appointment.setDoctor(doctor);
        Appointment appointmentDB = appointmentMapper.toUpdateAppointment(appointment, updateAppointmentDto);
        appointmentRepository.save(appointmentDB);
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setIsCancelled(true);
        appointmentRepository.save(appointment);
        String appointmentDetails = "Appointment with " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName() + " on " + appointment.getAppointmentDate() + " at " + appointment.getAppointmentStartTime();
        emailSenderService.sendAppointmentCancellationEmail(appointment.getPatient().getEmail(), appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(), appointmentDetails);
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void getNonApprovedAppointments() {
        List<Appointment> appointmentList = appointmentRepository.findUnconfirmedAppointments();
        for (Appointment appointment : appointmentList) {
            String appointmentDetails = "Appointment with " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName() + " on " + appointment.getAppointmentDate() + " at " + appointment.getAppointmentStartTime();
            String confirmationLink = "http://localhost:8080/api/appointments/confirm/" + appointment.getAppointmentId();
            emailSenderService.sendAppointmentConfirmationEmail(appointment.getPatient().getEmail(), appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName(), appointmentDetails, confirmationLink);
        }
    }

    @Transactional
    public void confirmAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setIsConfirmed(true);
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void addPrescriptionsToAppointment(Long appointmentId, CreatePrescriptionDto createPrescriptionsDto) {
        Appointment appointment = getAppointmentById(appointmentId);
        Prescription prescription = prescriptionMapper.toEntity(createPrescriptionsDto, appointment);
        appointment.getPrescriptions().add(prescription);
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void uploadFileToAppointment(Long appointmentId, MultipartFile multipartFile) throws IOException {
        Appointment appointment = getAppointmentById(appointmentId);
        File file = fileService.saveFile(multipartFile, appointment);
        appointment.getFiles().add(file);
        appointmentRepository.save(appointment);
    }

    private boolean hasDoctorAppointmentAtTime(User doctor, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Appointment> appointments = appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), date);
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentStartTime().isBefore(endTime) && appointment.getAppointmentEndTime().isAfter(startTime)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void deleteAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointmentRepository.delete(appointment);
    }
}