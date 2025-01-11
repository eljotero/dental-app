package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.availability.entity.Availability;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AvailableSlotsService {

    private final AppointmentService appointmentService;
    private final AvailabilityService availabilityService;

    public AvailableSlotsService(AppointmentService appointmentService, AvailabilityService availabilityService) {
        this.appointmentService = appointmentService;
        this.availabilityService = availabilityService;
    }

    public Map<String, String> getAvailableSlots(Long doctorId, LocalDate date) {
        List<Availability> availabilities = availabilityService.getDoctorAvailability(doctorId, date);
        List<Appointment> appointments = appointmentService.getDoctorsAppointmentsByDate(doctorId, date);
        Map<String, String> timeSlots = new LinkedHashMap<>();

        for (Availability availability : availabilities) {
            LocalTime startTime = availability.getAvailabilityStartTime();
            LocalTime endTime = availability.getAvailabilityEndTime();
            LocalTime breakStart = availability.getBrakeTimeStart();
            LocalTime breakEnd = availability.getBrakeTimeEnd();

            while (startTime.isBefore(endTime)) {
                LocalTime nextHour = startTime.plusHours(1);

                if (breakStart != null && breakEnd != null && !startTime.isBefore(breakStart) && startTime.isBefore(breakEnd)) {
                    startTime = breakEnd;
                } else {
                    timeSlots.put(startTime.toString(), nextHour.toString());
                    startTime = nextHour;
                }
            }
        }

        List<LocalTime> bookedTimes = appointments.stream()
                .map(Appointment::getAppointmentStartTime)
                .toList();

        for (String slotStart : new ArrayList<>(timeSlots.keySet())) {
            LocalTime start = LocalTime.parse(slotStart);
            if (bookedTimes.contains(start)) {
                timeSlots.remove(slotStart);
            }
        }

        return timeSlots;
    }

}