package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.dtos.AppointmentMapper;
import com.dentalapp.backend.model.appointment.dtos.CreateAppointmentDto;
import com.dentalapp.backend.model.appointment.dtos.UpdateAppointmentDto;
import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.appointment.exceptions.AppointmentNotFoundException;
import com.dentalapp.backend.model.appointment.exceptions.IllegalAppointmentDate;
import com.dentalapp.backend.model.appointment.repository.AppointmentRepository;
import com.dentalapp.backend.model.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final UserService userService;

    public AppointmentService(AppointmentRepository appointmentRepository, UserService userService) {
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
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

    public List<Appointment> getDoctorAppointmentsByDate(String doctorEmail, String date) {
        Long doctorId = userService.getDoctorByEmail(doctorEmail).getUserId();
        return appointmentRepository.findAllByDoctorIdAndDate(doctorId, date);
    }

    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));
    }

    public List<Appointment> getAppointmentsByDate(String date) {
        return appointmentRepository.findByDate(date);
    }

    @Transactional
    public Appointment createAppointment(CreateAppointmentDto createAppointmentDto) {
        User patient = userService.getPatientById(createAppointmentDto.getPatientId());
        User doctor = userService.getDoctorById(createAppointmentDto.getDoctorId());
        List<Appointment> appointments = appointmentRepository.findAllByDoctorIdAndDate(doctor.getUserId(), createAppointmentDto.getAppointmentDate().toString());
        appointments.forEach(a -> {
            if (a.getAppointmentDate().equals(createAppointmentDto.getAppointmentDate())) {
                throw new IllegalAppointmentDate("Doctor already has an appointment at this time");
            }
        });
        createAppointmentDto.setPatient(patient);
        createAppointmentDto.setDoctor(doctor);
        Appointment appointment = AppointmentMapper.toAppointment(createAppointmentDto);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void updateAppointment(UpdateAppointmentDto updateAppointmentDto, Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (updateAppointmentDto.getDoctorId() != null) {
            List<Appointment> appointments = appointmentRepository.findAllByDoctorIdAndDate(appointment.getDoctor().getUserId(), appointment.getAppointmentDate().toString());
            appointments.stream().filter(a -> !Objects.equals(a.getAppointmentId(), appointment.getAppointmentId())).forEach(a -> {
                if (a.getAppointmentDate().equals(updateAppointmentDto.getAppointmentDate())) {
                    throw new IllegalAppointmentDate("Doctor already has an appointment at this time");
                }
            });
        }
        appointment.setDoctor(userService.getDoctorById(updateAppointmentDto.getDoctorId()));
        if (updateAppointmentDto.getAppointmentDate() != null) {
            appointment.setAppointmentDate(updateAppointmentDto.getAppointmentDate());
        }
        if (updateAppointmentDto.getDescription() != null) {
            appointment.setDescription(updateAppointmentDto.getDescription());
        }
        appointmentRepository.save(appointment);
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setIsCancelled(true);
        appointmentRepository.save(appointment);
    }
}
