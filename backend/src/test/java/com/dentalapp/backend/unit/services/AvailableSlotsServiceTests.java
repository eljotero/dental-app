package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.availability.entity.Availability;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.AvailabilityService;
import com.dentalapp.backend.services.AvailableSlotsService;
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
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailableSlotsServiceTests {

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private AvailabilityService availabilityService;

    @InjectMocks
    private AvailableSlotsService availableSlotsService;

    private Long doctorId;

    private LocalDate date;

    private Availability availability;

    private Appointment appointment;

    @BeforeEach
    public void setUp() {
        doctorId = 1L;
        date = LocalDate.of(2024, 1, 1);

        availability = new Availability();
        availability.setAvailabilityStartTime(LocalTime.of(8, 0));
        availability.setAvailabilityEndTime(LocalTime.of(16, 0));
        availability.setBrakeTimeStart(LocalTime.of(12, 0));
        availability.setBrakeTimeEnd(LocalTime.of(13, 0));

        appointment = new Appointment();
        appointment.setAppointmentStartTime(LocalTime.of(9, 0));
    }

    @Test
    public void testGetAvailableSlots() {
        when(availabilityService.getDoctorAvailability(doctorId, date)).thenReturn(List.of(availability));
        when(appointmentService.getDoctorsAppointmentsByDate(doctorId, date)).thenReturn(List.of(appointment));

        Map<String, String> availableSlots = availableSlotsService.getAvailableSlots(doctorId, date);

        Assertions.assertEquals(6, availableSlots.size());
        Assertions.assertEquals("09:00", availableSlots.get("08:00"));
        Assertions.assertEquals("11:00", availableSlots.get("10:00"));
        Assertions.assertEquals("12:00", availableSlots.get("11:00"));
        Assertions.assertEquals("14:00", availableSlots.get("13:00"));
        Assertions.assertEquals("15:00", availableSlots.get("14:00"));
        Assertions.assertEquals("16:00", availableSlots.get("15:00"));
    }

    @Test
    public void testGetAvailableSlotsWithNoAvailability() {
        when(availabilityService.getDoctorAvailability(doctorId, date)).thenReturn(List.of());
        when(appointmentService.getDoctorsAppointmentsByDate(doctorId, date)).thenReturn(List.of());

        Map<String, String> availableSlots = availableSlotsService.getAvailableSlots(doctorId, date);

        Assertions.assertEquals(0, availableSlots.size());
    }

    @Test
    public void testGetAvailableSlotsWithNoAppointments() {
        when(availabilityService.getDoctorAvailability(doctorId, date)).thenReturn(List.of(availability));
        when(appointmentService.getDoctorsAppointmentsByDate(doctorId, date)).thenReturn(List.of());

        Map<String, String> availableSlots = availableSlotsService.getAvailableSlots(doctorId, date);

        Assertions.assertEquals(7, availableSlots.size());

        Assertions.assertEquals("09:00", availableSlots.get("08:00"));
        Assertions.assertEquals("10:00", availableSlots.get("09:00"));
        Assertions.assertEquals("11:00", availableSlots.get("10:00"));
        Assertions.assertEquals("12:00", availableSlots.get("11:00"));
        Assertions.assertEquals("14:00", availableSlots.get("13:00"));
        Assertions.assertEquals("15:00", availableSlots.get("14:00"));
        Assertions.assertEquals("16:00", availableSlots.get("15:00"));
    }
}