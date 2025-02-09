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

    private LocalDate startDate;

    private LocalDate endDate;

    private Availability availability;

    private Appointment appointment;

    @BeforeEach
    public void setUp() {
        doctorId = 1L;
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 1, 1);

        availability = new Availability();
        availability.setAvailabilityStartTime(LocalTime.of(8, 0));
        availability.setAvailabilityEndTime(LocalTime.of(16, 0));
        availability.setBrakeTimeStart(LocalTime.of(12, 0));
        availability.setBrakeTimeEnd(LocalTime.of(13, 0));
        availability.setAvailabilityDate(startDate);

        appointment = new Appointment();
        appointment.setAppointmentStartTime(LocalTime.of(9, 0));
    }

    @Test
    public void testGetAvailableSlots() {
        when(availabilityService.getDoctorAvailability(doctorId, startDate)).thenReturn(List.of(availability));
        when(appointmentService.getDoctorsAppointmentsByDate(doctorId, startDate)).thenReturn(List.of(appointment));

        Map<LocalDate, Map<String, String>> availableSlots = availableSlotsService.getAvailableSlots(doctorId, startDate, endDate);

        Assertions.assertEquals(1, availableSlots.size());
        Map<String, String> slots = availableSlots.get(startDate);
        Assertions.assertEquals("09:00", slots.get("08:00"));
        Assertions.assertEquals("11:00", slots.get("10:00"));
        Assertions.assertEquals("12:00", slots.get("11:00"));
        Assertions.assertEquals("14:00", slots.get("13:00"));
        Assertions.assertEquals("15:00", slots.get("14:00"));
        Assertions.assertEquals("16:00", slots.get("15:00"));
    }

    @Test
    public void testGetAvailableSlotsWithNoAvailability() {
        when(availabilityService.getDoctorAvailability(doctorId, startDate)).thenReturn(List.of());
        when(appointmentService.getDoctorsAppointmentsByDate(doctorId, startDate)).thenReturn(List.of());

        Map<LocalDate, Map<String, String>> availableSlots = availableSlotsService.getAvailableSlots(doctorId, startDate, endDate);

        Assertions.assertEquals(1, availableSlots.size());
        Map<String, String> slots = availableSlots.get(startDate);
        Assertions.assertNotNull(slots);
        Assertions.assertTrue(slots.isEmpty());
    }

    @Test
    public void testGetAvailableSlotsWithNoAppointments() {
        when(availabilityService.getDoctorAvailability(doctorId, startDate)).thenReturn(List.of(availability));
        when(appointmentService.getDoctorsAppointmentsByDate(doctorId, startDate)).thenReturn(List.of());

        Map<LocalDate, Map<String, String>> availableSlots = availableSlotsService.getAvailableSlots(doctorId, startDate, endDate);

        Assertions.assertEquals(1, availableSlots.size());
        Map<String, String> slots = availableSlots.get(startDate);
        Assertions.assertNotNull(slots);
        Assertions.assertEquals("09:00", slots.get("08:00"));
        Assertions.assertEquals("11:00", slots.get("10:00"));
        Assertions.assertEquals("12:00", slots.get("11:00"));
        Assertions.assertEquals("14:00", slots.get("13:00"));
        Assertions.assertEquals("15:00", slots.get("14:00"));
        Assertions.assertEquals("16:00", slots.get("15:00"));
    }
}