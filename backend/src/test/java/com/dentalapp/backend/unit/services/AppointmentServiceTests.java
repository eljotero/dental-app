package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.dtos.*;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.appointment.exceptions.AppointmentNotFoundException;
import com.dentalapp.backend.model.appointment.exceptions.IllegalAppointmentDate;
import com.dentalapp.backend.model.appointment.repository.AppointmentRepository;
import com.dentalapp.backend.model.enums.UserType;
import com.dentalapp.backend.model.file.entity.File;
import com.dentalapp.backend.model.invoice.entity.Invoice;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionDto;
import com.dentalapp.backend.model.prescription.dtos.CreatePrescriptionsDto;
import com.dentalapp.backend.model.prescription.entity.Prescription;
import com.dentalapp.backend.model.referral.entity.Referral;
import com.dentalapp.backend.model.user.entity.User;
import com.dentalapp.backend.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Mock
    private FileService fileService;

    @InjectMocks
    private AppointmentService appointmentService;

    private List<Appointment> appointments;

    private Appointment appointment;

    private Appointment appointment1;

    private User patient;

    private User doctor;

    private CreateAppointmentDto createAppointmentDto;

    private UpdateAppointmentDto updateAppointmentDto;

    private String patientEmail;

    private Invoice invoice;

    private Prescription prescription;

    private Referral referral;

    @BeforeEach
    public void setUp() {
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

        appointment = new Appointment();
        appointment.setAppointmentDate(LocalDate.of(2021, 1, 1));
        appointment.setAppointmentStartTime(LocalTime.of(12, 0));
        appointment.setAppointmentEndTime(LocalTime.of(13, 0));

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        appointment1 = new Appointment();
        appointment1.setAppointmentStartTime(LocalTime.of(14, 0));
        appointment1.setAppointmentEndTime(LocalTime.of(15, 0));
        appointment.setAppointmentDate(LocalDate.of(2021, 1, 1));

        appointments = List.of(appointment, appointment1);

        createAppointmentDto = new CreateAppointmentDto();
        createAppointmentDto.setDoctorId(doctor.getUserId());
        createAppointmentDto.setAppointmentDate(LocalDate.of(2021, 1, 1));
        createAppointmentDto.setAppointmentStartTime("12:00");
        createAppointmentDto.setAppointmentEndTime("13:00");

        updateAppointmentDto = new UpdateAppointmentDto();
        updateAppointmentDto.setDoctorId(doctor.getUserId());
        updateAppointmentDto.setAppointmentDate(LocalDate.of(2021, 1, 1));
        updateAppointmentDto.setAppointmentDescription("test");
        updateAppointmentDto.setAppointmentStartTime("12:00");
        updateAppointmentDto.setAppointmentEndTime("13:00");

        patientEmail = "test@mail.com";

        List<File> files = new java.util.ArrayList<>(List.of());
        File file = new File();
        files.add(file);
        appointment.setFiles(files);

        invoice = new Invoice();
        appointment.setInvoice(invoice);

        referral = new Referral();
        appointment.setReferrals(List.of(referral));

        prescription = new Prescription();
        appointment.setPrescriptions(List.of(prescription));
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

        List<GetAppointmentDto> expected = appointments.stream()
                .map(AppointmentMapper::toGetAppointmentDto)
                .collect(Collectors.toList());

        List<GetAppointmentDto> result = appointmentService.getPatientAppointments(patient.getEmail());

        Assertions.assertEquals(expected, result);
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

//    @Test
//    public void testAddPrescriptionsToAppointment() {
//        CreatePrescriptionsDto createPrescriptionsDto = new CreatePrescriptionsDto();
//        CreatePrescriptionDto createPrescriptionDto = new CreatePrescriptionDto();
//        createPrescriptionDto.setMedicineName("Medicine");
//        createPrescriptionDto.setDosage("Dosage");
//        createPrescriptionsDto.setCreatePrescriptionsDtoList(List.of(createPrescriptionDto));
//        Prescription prescription = new Prescription();
//        when(prescriptionService.addPrescriptions(createPrescriptionsDto, appointment)).thenReturn(List.of(prescription));
//        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
//        appointmentService.addPrescriptionsToAppointment(1L, createPrescriptionsDto);
//        verify(appointmentRepository).save(any(Appointment.class));
//    }

    @Test
    public void testSaveAppointment() {
        appointmentService.saveAppointment(appointment);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    public void testUpdateFileToAppointment() throws IOException {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        File file = new File();
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        when(fileService.saveFile(multipartFile, appointment)).thenReturn(file);
        appointmentService.uploadFileToAppointment(1L, multipartFile);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    public void testGetDoctorsAppointmentsByDate() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), date)).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getDoctorsAppointmentsByDate(doctor.getUserId(), date));
    }

    @Test
    public void testGetAppointmentByIdDto() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        GetAppointmentDtoV2 expectedDto = AppointmentMapper.toGetAppointmentDtoV2(appointment);
        GetAppointmentDtoV2 result = appointmentService.getAppointmentByIdDto(1L);
        Assertions.assertEquals(result.getAppointmentDate(), expectedDto.getAppointmentDate());
        Assertions.assertEquals(result.getAppointmentStartTime(), expectedDto.getAppointmentStartTime());
        Assertions.assertEquals(result.getAppointmentEndTime(), expectedDto.getAppointmentEndTime());
        Assertions.assertEquals(result.getDescription(), expectedDto.getDescription());
        Assertions.assertEquals(result.getDoctorName(), expectedDto.getDoctorName());
        Assertions.assertEquals(result.getDoctorLastName(), expectedDto.getDoctorLastName());
        Assertions.assertEquals(result.getDoctorPhoneNumber(), expectedDto.getDoctorPhoneNumber());
        Assertions.assertEquals(result.isCancelled(), expectedDto.isCancelled());
        Assertions.assertEquals(result.isConfirmed(), expectedDto.isConfirmed());
        Assertions.assertEquals(result.isPaid(), expectedDto.isPaid());
    }

    @Test
    public void testGetAppointmentByIdDtoNotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(AppointmentNotFoundException.class, () -> appointmentService.getAppointmentByIdDto(1L));
    }

}