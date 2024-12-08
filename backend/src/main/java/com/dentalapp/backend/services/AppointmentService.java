package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.dtos.AppointmentMapper;
import com.dentalapp.backend.model.appointment.dtos.CreateAppointmentDto;
import com.dentalapp.backend.model.appointment.dtos.UpdateAppointmentDto;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.appointment.exceptions.AppointmentNotFoundException;
import com.dentalapp.backend.model.appointment.exceptions.IllegalAppointmentDate;
import com.dentalapp.backend.model.appointment.repository.AppointmentRepository;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionsDto;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import com.dentalapp.backend.model.user.entity.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final UserService userService;

    private final AvailabilityService availabilityService;

    private final EmailSenderService emailSenderService;

    private final InvoiceService invoiceService;

    private final PrescriptionService prescriptionService;

    public AppointmentService(AppointmentRepository appointmentRepository, UserService userService, AvailabilityService availabilityService, EmailSenderService emailSenderService, InvoiceService invoiceService, PrescriptionService prescriptionService) {
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
        this.availabilityService = availabilityService;
        this.emailSenderService = emailSenderService;
        this.invoiceService = invoiceService;
        this.prescriptionService = prescriptionService;
    }

    public List<Appointment> getAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getPatientAppointments(String patientEmail) {
        Long patientId = userService.getPatientByEmail(patientEmail).getUserId();
        return appointmentRepository.findAllByPatientId(patientId);
    }

    public List<Appointment> getDoctorAppointments(String doctorEmail) {
        Long doctorId = userService.getDoctorByEmail(doctorEmail).getUserId();
        return appointmentRepository.findAllByDoctorId(doctorId);
    }

    public List<Appointment> getDoctorAppointmentsByDate(String doctorEmail, LocalDate date) {
        Long doctorId = userService.getDoctorByEmail(doctorEmail).getUserId();
        return appointmentRepository.findAllByDoctorIdAndDate(doctorId, date);
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
        Appointment appointment = AppointmentMapper.toAppointment(createAppointmentDto);
        Invoice invoice = invoiceService.createInvoice();
        appointment.setInvoice(invoice);
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void updateAppointment(UpdateAppointmentDto updateAppointmentDto, Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (updateAppointmentDto.getDoctorId() != null) {
            List<Appointment> appointments = appointmentRepository.findAllByDoctorIdAndDate(appointment.getDoctor().getUserId(), appointment.getAppointmentDate());
            appointments.stream().filter(a -> !Objects.equals(a.getAppointmentId(), appointment.getAppointmentId())).forEach(a -> {
                if (a.getAppointmentDate().equals(updateAppointmentDto.getAppointmentDate())) {
                    throw new IllegalAppointmentDate("Doctor already has an appointment at this time");
                }
            });
        }
        appointment.setDoctor(userService.getDoctorById(updateAppointmentDto.getDoctorId()));
        Appointment appointmentDB = AppointmentMapper.toUpdateAppointment(appointment, updateAppointmentDto);
        appointmentRepository.save(appointmentDB);
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setIsCancelled(true);
        appointmentRepository.save(appointment);
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
    public void addPrescriptionsToAppointment(Long appointmentId, CreatePrescriptionsDto createPrescriptionsDto) {
        List<Prescription> prescriptionList = prescriptionService.addPrescriptions(createPrescriptionsDto);
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setPrescriptions(prescriptionList);
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
}