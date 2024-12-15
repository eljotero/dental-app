package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.dtos.CreateAppointmentDto;
import com.dentalapp.backend.model.appointment.dtos.UpdateAppointmentDto;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.appointment.exceptions.AppointmentNotFoundException;
import com.dentalapp.backend.model.appointment.exceptions.IllegalAppointmentDate;
import com.dentalapp.backend.model.appointment.repository.AppointmentRepository;
import com.dentalapp.backend.model.enums.UserType;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionDto;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionsDto;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserService userService;

    @Mock
    private AvailabilityService availabilityService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private PrescriptionService prescriptionService;

    @InjectMocks
    private AppointmentService appointmentService;

    List<Appointment> appointments;

    Appointment appointment;

    Appointment appointment1;

    User patient;

    User doctor;

    CreateAppointmentDto createAppointmentDto;

    UpdateAppointmentDto updateAppointmentDto;

    String patientEmail;

    @BeforeEach
    public void setUp() {
        appointment = new Appointment();
        appointment1 = new Appointment();
        appointments = List.of(appointment, appointment1);
        patient = new User();
        patient.setUserId(1L);
        patient.setUserType(UserType.PATIENT);
        patient.setEmail("test@mail.com");
        doctor = new User();
        doctor.setUserId(2L);
        doctor.setUserType(UserType.DOCTOR);
        doctor.setEmail("test2@mail.com");
        doctor.setFirstName("test");
        doctor.setLastName("test");
        createAppointmentDto = new CreateAppointmentDto();
        createAppointmentDto.setDoctorId(doctor.getUserId());
        createAppointmentDto.setAppointmentDate(LocalDate.of(2021, 1, 1));
        createAppointmentDto.setAppointmentStartTime("12:00");
        createAppointmentDto.setAppointmentEndTime("13:00");
        updateAppointmentDto = new UpdateAppointmentDto();
        updateAppointmentDto.setDoctorId(doctor.getUserId());
        updateAppointmentDto.setAppointmentDate(LocalDate.of(2021, 1, 1));
        updateAppointmentDto.setDescription("test");
        updateAppointmentDto.setAppointmentStartTime("12:00");
        updateAppointmentDto.setAppointmentEndTime("13:00");
        appointment.setAppointmentDate(LocalDate.of(2021, 1, 1));
        appointment.setAppointmentStartTime(LocalTime.of(12, 0));
        appointment.setAppointmentEndTime(LocalTime.of(13, 0));
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        patientEmail = "test@mail.com";
    }

    @Test
    public void testGetAppointments() {
        when(appointmentRepository.findAll()).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getAppointments(null));
    }

    @Test
    public void testGetAppointmentsDateNotNull() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        when(appointmentRepository.findAllByDate(date)).thenReturn(appointments);
        List<Appointment> result = appointmentService.getAppointments(date);
        Assertions.assertEquals(appointments, result);
    }

    @Test
    public void testGetPatientAppointments() {
        when(userService.getPatientByEmail(patient.getEmail())).thenReturn(patient);
        when(appointmentRepository.findAllByPatientId(patient.getUserId())).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getPatientAppointments(patient.getEmail()));
    }

    @Test
    public void testGetDoctorAppointments() {
        when(userService.getDoctorByEmail(doctor.getEmail())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorId(doctor.getUserId())).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getDoctorAppointments(doctor.getEmail(), null));
    }

    @Test
    public void testGetDoctorAppointmentsDateNotNull() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        when(userService.getDoctorByEmail(doctor.getEmail())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), date)).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getDoctorAppointments(doctor.getEmail(), date));
    }

    @Test
    public void testGetDoctorAppointmentsByDate() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        when(userService.getDoctorByEmail(doctor.getEmail())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), date)).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getDoctorAppointmentsByDate(doctor.getEmail(), date));
    }

    @Test
    public void testGetAppointmentById() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        Assertions.assertEquals(appointment, appointmentService.getAppointmentById(1L));
    }

    @Test
    public void testGetAppointmentByIdNotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(AppointmentNotFoundException.class, () -> appointmentService.getAppointmentById(1L));
    }

    @Test
    public void testGetAppointmentsByDate() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        when(appointmentRepository.findByDate(date)).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getAppointmentsByDate(date));
    }

    @Test
    public void testCreateAppointment() {
        when(userService.getPatientByEmail(patientEmail)).thenReturn(patient);
        when(userService.getDoctorById(doctor.getUserId())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), createAppointmentDto.getAppointmentDate())).thenReturn(List.of());
        when(availabilityService.isDoctorAvailable(doctor, createAppointmentDto.getAppointmentDate(), LocalTime.parse(createAppointmentDto.getAppointmentStartTime()), LocalTime.parse(createAppointmentDto.getAppointmentEndTime()))).thenReturn(true);
        appointmentService.createAppointment(createAppointmentDto, patientEmail);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    public void testCreateAppointmentDoctorHasAppointment() {
        when(userService.getPatientByEmail(patientEmail)).thenReturn(patient);
        when(userService.getDoctorById(doctor.getUserId())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), createAppointmentDto.getAppointmentDate())).thenReturn(appointments);
        Assertions.assertThrows(IllegalAppointmentDate.class, () -> appointmentService.createAppointment(createAppointmentDto, patientEmail));
    }

    @Test
    public void testCreateAppointmentDoctorIsNotAvailable() {
        when(userService.getPatientByEmail(patientEmail)).thenReturn(patient);
        when(userService.getDoctorById(doctor.getUserId())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), createAppointmentDto.getAppointmentDate())).thenReturn(List.of());
        when(availabilityService.isDoctorAvailable(doctor, createAppointmentDto.getAppointmentDate(), LocalTime.parse(createAppointmentDto.getAppointmentStartTime()), LocalTime.parse(createAppointmentDto.getAppointmentEndTime()))).thenReturn(false);
        Assertions.assertThrows(IllegalAppointmentDate.class, () -> appointmentService.createAppointment(createAppointmentDto, patientEmail));
    }

    @Test
    public void testUpdateAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        when(userService.getDoctorById(doctor.getUserId())).thenReturn(doctor);
        appointmentService.updateAppointment(updateAppointmentDto, 1L);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    public void testUpdateAppointmentDoctorIsBusy() {
        appointment1.setAppointmentDate(LocalDate.of(2021, 1, 1));
        appointment1.setAppointmentStartTime(LocalTime.of(12, 0));
        appointment1.setAppointmentEndTime(LocalTime.of(13, 0));
        appointment1.setAppointmentId(2L);
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), updateAppointmentDto.getAppointmentDate())).thenReturn(appointments);
        Assertions.assertThrows(IllegalAppointmentDate.class, () -> appointmentService.updateAppointment(updateAppointmentDto, 1L));
    }

    @Test
    public void testCancelAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        appointmentService.cancelAppointment(1L);
        Assertions.assertEquals(true, appointment.getIsCancelled());
    }

    @Test
    public void testGetNonApprovedAppointments() {
        when(appointmentRepository.findUnconfirmedAppointments()).thenReturn(List.of(appointment));
        appointmentService.getNonApprovedAppointments();
        verify(appointmentRepository).findUnconfirmedAppointments();
    }

    @Test
    public void testConfirmAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        appointmentService.confirmAppointment(1L);
        Assertions.assertEquals(true, appointment.getIsConfirmed());
    }

    @Test
    public void testAddPrescriptionsToAppointment() {
        CreatePrescriptionsDto createPrescriptionsDto = new CreatePrescriptionsDto();
        CreatePrescriptionDto createPrescriptionDto = new CreatePrescriptionDto();
        createPrescriptionDto.setMedicine("Medicine");
        createPrescriptionDto.setDosage("Dosage");
        createPrescriptionsDto.setCreatePrescriptionsDtoList(List.of(createPrescriptionDto));
        Prescription prescription = new Prescription();
        when(prescriptionService.addPrescriptions(createPrescriptionsDto, appointment)).thenReturn(List.of(prescription));
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        appointmentService.addPrescriptionsToAppointment(1L, createPrescriptionsDto);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    public void testSaveAppointment() {
        appointmentService.saveAppointment(appointment);
        verify(appointmentRepository).save(appointment);
    }

}