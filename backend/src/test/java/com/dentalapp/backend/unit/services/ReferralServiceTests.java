package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.referral.dtos.CreateReferralDto;
import com.dentalapp.backend.model.referral.dtos.UpdateReferralDto;
import com.dentalapp.backend.model.referral.entity.Referral;
import com.dentalapp.backend.model.referral.exception.ReferralNotFoundException;
import com.dentalapp.backend.model.referral.repository.ReferralRepository;
import com.dentalapp.backend.services.AppointmentService;
import com.dentalapp.backend.services.ReferralService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReferralServiceTests {
    @Mock
    private ReferralRepository referralRepository;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private ReferralService referralService;

    private Referral referral;

    private final String email = "test@mail.com";

    @BeforeEach
    public void setUp() {
        referral = new Referral();
        referral.setReferralId(1L);
    }

    @Test
    public void testFindAllReferrals() {
        when(referralRepository.findAll()).thenReturn(List.of(referral));
        List<Referral> referrals = referralService.findAll();
        Assertions.assertEquals(1, referrals.size());
    }

    @Test
    public void testFindReferralById() {
        when(referralRepository.findById(1L)).thenReturn(java.util.Optional.of(referral));
        Referral foundReferral = referralService.findById(1L);
        Assertions.assertEquals(referral, foundReferral);
    }

    @Test
    public void testFindAllByPatient() {
        when(referralRepository.findAllByPatientEmail(email)).thenReturn(List.of(referral));
        List<Referral> referrals = referralService.findAllByPatient(email);
        Assertions.assertEquals(1, referrals.size());
    }

    @Test
    public void testFindAllByDoctor() {
        when(referralRepository.findAllByDoctorEmail(email)).thenReturn(List.of(referral));
        List<Referral> referrals = referralService.findAllByDoctor(email);
        Assertions.assertEquals(1, referrals.size());
    }

    @Test
    public void testFindReferralByNonExistentId() {
        when(referralRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(ReferralNotFoundException.class, () -> referralService.findById(1L));
    }

    @Test
    public void testCreateReferral() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        CreateReferralDto createReferralDto = new CreateReferralDto();
        createReferralDto.setAppointmentId(1L);
        when(appointmentService.getAppointmentById(createReferralDto.getAppointmentId())).thenReturn(appointment);
        referralService.createReferral(createReferralDto);
        verify(referralRepository).saveAll(any());
        verify(appointmentService).saveAppointment(appointment);
    }

    @Test
    public void testUpdateReferral() {
        UpdateReferralDto updateReferralDto = new UpdateReferralDto();
        when(referralRepository.findById(1L)).thenReturn(java.util.Optional.of(referral));
        referralService.updateReferral(1L, updateReferralDto);
        verify(referralRepository).save(referral);
    }

    @Test
    public void testDeleteReferral() {
        referralService.deleteReferral(1L);
        verify(referralRepository).deleteById(1L);
    }
}
