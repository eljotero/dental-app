package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.dtos.*;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.appointment.exceptions.AppointmentNotFoundException;
import com.dentalapp.backend.model.appointment.exceptions.IllegalAppointmentDate;
import com.dentalapp.backend.model.appointment.repository.AppointmentRepository;
import com.dentalapp.backend.model.enums.UserType;
import com.dentalapp.backend.model.file.entity.File;
import com.dentalapp.backend.model.invoice.entity.Invoice;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTests {

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

    @Mock
    private AppointmentMapper appointmentMapper;

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

    private String doctorEmail;

    @BeforeEach
    void setUp() {
        doctorEmail = "test@mail.com";

        patient = new User();
        patient.setUserId(1L);
        patient.setUserType(UserType.PATIENT);
        patient.setEmail("test@mail.com");
        patient.setFirstName("test");
        patient.setLastName("test");

        doctor = new User();
        doctor.setUserId(2L);
        doctor.setUserType(UserType.DOCTOR);
        doctor.setEmail(doctorEmail);
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
        appointment1.setDoctor(doctor);
        appointment1.setPatient(patient);
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
    void testGetAppointments() {
        when(appointmentRepository.findAll()).thenReturn(appointments);

        List<GetAppointmentDto> result = appointmentService.getAppointments(null);

        Assertions.assertEquals(2, result.size());
        verify(appointmentRepository).findAll();
    }


    @Test
    void testGetAppointmentsDateNotNull() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        when(appointmentRepository.findAllByDate(date)).thenReturn(appointments);

        List<GetAppointmentDto> result = appointmentService.getAppointments(date);

        Assertions.assertEquals(2, result.size());
        verify(appointmentRepository).findAllByDate(date);
    }

    @Test
    void testGetPatientAppointments() {
        when(userService.getPatientByEmail(patient.getEmail())).thenReturn(patient);
        when(appointmentRepository.findAllByPatientId(patient.getUserId())).thenReturn(appointments);

        List<GetAppointmentDto> result = appointmentService.getPatientAppointments(patient.getEmail());

        Assertions.assertEquals(2, result.size());
        verify(appointmentRepository).findAllByPatientId(patient.getUserId());
    }

    @Test
    void testGetDoctorAppointments() {
        when(userService.getDoctorByEmail(doctor.getEmail())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorId(doctor.getUserId())).thenReturn(appointments);

        List<GetAppointmentDtoV3> result = appointmentService.getDoctorAppointments(doctor.getEmail(), null);

        Assertions.assertEquals(2, result.size());
        verify(appointmentRepository).findAllByDoctorId(doctor.getUserId());
    }

    @Test
    void testGetDoctorAppointmentsDateNotNull() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        when(userService.getDoctorByEmail(doctor.getEmail())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), date)).thenReturn(appointments);

        List<GetAppointmentDtoV3> result = appointmentService.getDoctorAppointments(doctor.getEmail(), date);

        Assertions.assertEquals(2, result.size());
        verify(appointmentRepository).findAllByDoctorIdAndDate(doctor.getUserId(), date);
    }

    @Test
    void testCreateAppointment() {
        when(userService.getPatientByEmail(patientEmail)).thenReturn(patient);
        when(userService.getDoctorById(doctor.getUserId())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), createAppointmentDto.getAppointmentDate())).thenReturn(List.of());
        when(availabilityService.isDoctorAvailable(doctor, createAppointmentDto.getAppointmentDate(), LocalTime.parse(createAppointmentDto.getAppointmentStartTime()), LocalTime.parse(createAppointmentDto.getAppointmentEndTime()))).thenReturn(true);
        when(invoiceService.createInvoice()).thenReturn(invoice);

        Appointment newAppointment = new Appointment();
        when(appointmentMapper.toAppointment(createAppointmentDto)).thenReturn(newAppointment);

        appointmentService.createAppointment(createAppointmentDto, patientEmail);

        verify(appointmentRepository).save(any(Appointment.class));
    }


    @Test
    void testUpdateAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        when(userService.getDoctorByEmail(doctorEmail)).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorAndDate(doctor, updateAppointmentDto.getAppointmentDate())).thenReturn(List.of());
        when(appointmentMapper.toUpdateAppointment(appointment, updateAppointmentDto)).thenReturn(appointment);

        appointmentService.updateAppointment(updateAppointmentDto, 1L, doctorEmail);

        verify(appointmentRepository).save(any(Appointment.class));
    }


    @Test
    void testGetAppointmentByIdDto() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));

        GetAppointmentDtoV2 expectedDto = new GetAppointmentDtoV2();
        when(appointmentMapper.toGetAppointmentDtoV2(appointment)).thenReturn(expectedDto);

        GetAppointmentDtoV2 result = appointmentService.getAppointmentByIdDto(1L);

        Assertions.assertNotNull(result);
        verify(appointmentRepository).findById(1L);
        verify(appointmentMapper).toGetAppointmentDtoV2(appointment);
    }


    @Test
    void testGetDoctorAppointmentsByDate() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        when(userService.getDoctorByEmail(doctor.getEmail())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), date)).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getDoctorAppointmentsByDate(doctor.getEmail(), date));
    }

    @Test
    void testGetAppointmentById() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        Assertions.assertEquals(appointment, appointmentService.getAppointmentById(1L));
    }

    @Test
    void testGetAppointmentByIdNotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(AppointmentNotFoundException.class, () -> appointmentService.getAppointmentById(1L));
    }

    @Test
    void testGetAppointmentsByDate() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        when(appointmentRepository.findByDate(date)).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getAppointmentsByDate(date));
    }

    @Test
    void testCreateAppointmentDoctorHasAppointment() {
        when(userService.getPatientByEmail(patientEmail)).thenReturn(patient);
        when(userService.getDoctorById(doctor.getUserId())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), createAppointmentDto.getAppointmentDate())).thenReturn(appointments);
        Assertions.assertThrows(IllegalAppointmentDate.class, () -> appointmentService.createAppointment(createAppointmentDto, patientEmail));
    }

    @Test
    void testCreateAppointmentDoctorIsNotAvailable() {
        when(userService.getPatientByEmail(patientEmail)).thenReturn(patient);
        when(userService.getDoctorById(doctor.getUserId())).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), createAppointmentDto.getAppointmentDate())).thenReturn(List.of());
        when(availabilityService.isDoctorAvailable(doctor, createAppointmentDto.getAppointmentDate(), LocalTime.parse(createAppointmentDto.getAppointmentStartTime()), LocalTime.parse(createAppointmentDto.getAppointmentEndTime()))).thenReturn(false);
        Assertions.assertThrows(IllegalAppointmentDate.class, () -> appointmentService.createAppointment(createAppointmentDto, patientEmail));
    }

    @Test
    void testUpdateAppointmentDoctorIsBusy() {
        appointment.setAppointmentDate(LocalDate.of(2021, 1, 1));
        appointment.setAppointmentStartTime(LocalTime.of(12, 0));
        appointment.setAppointmentEndTime(LocalTime.of(13, 0));
        appointment.setAppointmentId(1L);

        appointment1.setAppointmentDate(LocalDate.of(2021, 1, 1));
        appointment1.setAppointmentStartTime(LocalTime.of(12, 0));
        appointment1.setAppointmentEndTime(LocalTime.of(13, 0));
        appointment1.setAppointmentId(2L);

        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        when(userService.getDoctorByEmail(doctorEmail)).thenReturn(doctor);
        when(appointmentRepository.findAllByDoctorAndDate(doctor, updateAppointmentDto.getAppointmentDate())).thenReturn(List.of(appointment1));

        Assertions.assertThrows(IllegalAppointmentDate.class, () -> appointmentService.updateAppointment(updateAppointmentDto, 1L, doctorEmail));
    }

    @Test
    void testCancelAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        appointmentService.cancelAppointment(1L);
        Assertions.assertEquals(true, appointment.getIsCancelled());
    }

    @Test
    void testGetNonApprovedAppointments() {
        when(appointmentRepository.findUnconfirmedAppointments()).thenReturn(List.of(appointment));
        appointmentService.getNonApprovedAppointments();
        verify(appointmentRepository).findUnconfirmedAppointments();
    }

    @Test
    void testConfirmAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        appointmentService.confirmAppointment(1L);
        Assertions.assertEquals(true, appointment.getIsConfirmed());
    }

    @Test
    void testSaveAppointment() {
        appointmentService.saveAppointment(appointment);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void testUpdateFileToAppointment() {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
        File file = new File();
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.of(appointment));
        when(fileService.saveFile(multipartFile, appointment)).thenReturn(file);
        appointmentService.uploadFileToAppointment(1L, multipartFile);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void testGetDoctorsAppointmentsByDate() {
        LocalDate date = LocalDate.of(2021, 1, 1);
        when(appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), date)).thenReturn(appointments);
        Assertions.assertEquals(appointments, appointmentService.getDoctorsAppointmentsByDate(doctor.getUserId(), date));
    }

    @Test
    void testGetAppointmentByIdDtoNotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(AppointmentNotFoundException.class, () -> appointmentService.getAppointmentByIdDto(1L));
    }

}