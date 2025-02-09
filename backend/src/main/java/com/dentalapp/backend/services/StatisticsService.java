package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.enums.PaymentMethod;
import com.dentalapp.backend.model.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final AppointmentService appointmentService;

    public StatisticsService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public Map<String, Object> getStatistics(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("patientVisitsByGender", getPatientVisitsByGender(startDate, endDate));
        statistics.put("paymentTypeRatio", getPaymentTypeRatio(startDate, endDate));
        statistics.put("patientVisitsByAgeGroup", getPatientVisitsByAgeGroup(startDate, endDate));
        statistics.put("weeklyRevenue", getWeeklyRevenue(startDate, endDate));
        return statistics;
    }

    public Map<Boolean, Long> getPatientVisitsByGender(LocalDate startDate, LocalDate endDate) {
        List<User> patients = appointmentService.getAppointmentsByDateRange(startDate, endDate).stream()
                .map(Appointment::getPatient)
                .toList();

        return patients.stream()
                .collect(Collectors.groupingBy(User::getSex, Collectors.counting()));
    }

    public Map<PaymentMethod, Long> getPaymentTypeRatio(LocalDate startDate, LocalDate endDate) {
        return appointmentService.getAppointmentsByDateRange(startDate, endDate).stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getInvoice().getPaymentMethod(), Collectors.counting()));
    }

    public Map<String, Long> getPatientVisitsByAgeGroup(LocalDate startDate, LocalDate endDate) {
        List<User> patients = appointmentService.getAppointmentsByDateRange(startDate, endDate).stream()
                .map(Appointment::getPatient)
                .toList();

        return patients.stream()
                .collect(Collectors.groupingBy(patient -> {
                    int age = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();
                    if (age < 18) {
                        return "Under 18";
                    } else if (age < 30) {
                        return "18-29";
                    } else if (age < 45) {
                        return "30-44";
                    } else if (age < 60) {
                        return "45-59";
                    } else {
                        return "60+";
                    }
                }, Collectors.counting()));
    }

    public Map<String, Double> getWeeklyRevenue(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> weeklyRevenue = new HashMap<>();
        LocalDate currentStartDate = startDate;

        while (!currentStartDate.isAfter(endDate)) {
            LocalDate currentEndDate = currentStartDate.plusDays(6);
            if (currentEndDate.isAfter(endDate)) {
                currentEndDate = endDate;
            }

            double revenue = appointmentService.getAppointmentsByDateRange(currentStartDate, currentEndDate).stream()
                    .mapToDouble(appointment -> appointment.getInvoice().getPrice())
                    .sum();

            weeklyRevenue.put(currentStartDate + " to " + currentEndDate, revenue);
            currentStartDate = currentEndDate.plusDays(1);
        }

        return weeklyRevenue;
    }
}