package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.dtos.CreateAppointmentDto;
import com.dentalapp.backend.model.appointment.dtos.UpdateAppointmentDto;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.appointment.exceptions.AppointmentNotFoundException;
import com.dentalapp.backend.model.appointment.exceptions.IllegalAppointmentDate;
import com.dentalapp.backend.model.appointment.repository.AppointmentRepository;
import com.dentalapp.backend.model.enums.UserType;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AppointmentServiceTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AppointmentService appointmentService;

    List<Appointment> appointments;

    Appointment appointment;

    Appointment appointment1;

    User patient;

    User doctor;

    CreateAppointmentDto createAppointmentDto;

    UpdateAppointmentDto updateAppointmentDto;

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
        createAppointmentDto = new CreateAppointmentDto();
        createAppointmentDto.setPatientId(patient.getUserId());
        createAppointmentDto.setDoctorId(doctor.getUserId());
        createAppointmentDto.setAppointmentDate(LocalDateTime.of(2021, 1, 1, 12, 0));
        createAppointmentDto.setAppointmentDuration(LocalTime.of(1, 0));
        updateAppointmentDto = new UpdateAppointmentDto();
        updateAppointmentDto.setDoctorId(doctor.getUserId());
        updateAppointmentDto.setAppointmentDate(LocalDateTime.of(2021, 1, 1, 12, 0));
        updateAppointmentDto.setDescription("test");
        appointment.setAppointmentDate(LocalDateTime.of(2021, 1, 1, 12, 0));
        appointment.setDoctor(doctor);
    }

    @Test
    public void testGetAppointments() {
        when(appointmentRepository.findAll()).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getAppointments());
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
        Assertions.assertEquals(appointments, appointmentService.getDoctorAppointments(doctor.getEmail()));
    }

    @Test
    public void testGetDoctorAppointmentsByDate() {
        when(userService.getDoctorByEmail(doctor.getEmail())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), "2021-01-01")).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getDoctorAppointmentsByDate(doctor.getEmail(), "2021-01-01"));
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
        when(appointmentRepository.findByDate("2021-01-01")).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getAppointmentsByDate("2021-01-01"));
    }

    @Test
    public void testCreateAppointment() {
        when(userService.getPatientById(patient.getUserId())).thenReturn(patient);
        when(userService.getDoctorById(doctor.getUserId())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), createAppointmentDto.getAppointmentDate().toString())).thenReturn(List.of());
        when(appointmentRepository.save(appointment)).thenReturn(appointment);
        appointmentService.createAppointment(createAppointmentDto);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    public void testCreateAppointmentDoctorHasAppointment() {
        when(userService.getPatientById(patient.getUserId())).thenReturn(patient);
        when(userService.getDoctorById(doctor.getUserId())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), createAppointmentDto.getAppointmentDate().toString())).thenReturn(appointments);
        Assertions.assertThrows(IllegalAppointmentDate.class, () -> appointmentService.createAppointment(createAppointmentDto));
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
        appointment1.setAppointmentDate(LocalDateTime.of(2021, 1, 1, 12, 0));
        appointment1.setAppointmentId(2L);
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        when(userService.getDoctorById(doctor.getUserId())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), updateAppointmentDto.getAppointmentDate().toString())).thenReturn(appointments);
        Assertions.assertThrows(IllegalAppointmentDate.class, () -> appointmentService.updateAppointment(updateAppointmentDto, 1L));
    }

    @Test
    public void testCancelAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        appointmentService.cancelAppointment(1L);
        Assertions.assertEquals(true, appointment.getIsCancelled());
    }

}
