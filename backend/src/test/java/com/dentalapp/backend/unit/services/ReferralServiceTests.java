package com.dentalapp.backend.unit.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.referral.dtos.CreateReferralDto;
import com.dentalapp.backend.model.referral.dtos.GetReferralDto;
import com.dentalapp.backend.model.referral.dtos.ReferralMapper;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReferralServiceTests {

    @Mock
    private ReferralRepository referralRepository;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private ReferralMapper referralMapper;

    @InjectMocks
    private ReferralService referralService;

    private Referral referral;

    private final String email = "test@mail.com";

    @BeforeEach
    void setUp() {
        referral = new Referral();
        referral.setReferralId(1L);
    }

    @Test
    void testFindAllReferrals() {
        when(referralRepository.findAll()).thenReturn(List.of(referral));
        List<GetReferralDto> referrals = referralService.findAll();
        Assertions.assertEquals(1, referrals.size());
    }

    @Test
    void testFindReferralById() {
        GetReferralDto expectedDto = new GetReferralDto();

        when(referralRepository.findById(1L)).thenReturn(java.util.Optional.of(referral));
        when(referralMapper.toGetReferralDto(referral)).thenReturn(expectedDto);

        GetReferralDto foundReferral = referralService.findById(1L);

        Assertions.assertEquals(expectedDto, foundReferral);
        verify(referralRepository).findById(1L);
        verify(referralMapper).toGetReferralDto(referral);
    }

    @Test
    void testFindAllByPatient() {
        when(referralRepository.findAllByPatientEmail(email)).thenReturn(List.of(referral));
        List<GetReferralDto> referrals = referralService.findAllByPatient(email);
        Assertions.assertEquals(1, referrals.size());
    }

    @Test
    void testFindAllByDoctor() {
        when(referralRepository.findAllByDoctorEmail(email)).thenReturn(List.of(referral));
        List<GetReferralDto> referrals = referralService.findAllByDoctor(email);
        Assertions.assertEquals(1, referrals.size());
    }

    @Test
    void testFindReferralByNonExistentId() {
        when(referralRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(ReferralNotFoundException.class, () -> referralService.findById(1L));
    }

    @Test
    void testCreateReferral() {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(1L);
        CreateReferralDto createReferralDto = new CreateReferralDto();
        createReferralDto.setAppointmentId(1L);
        when(appointmentService.getAppointmentById(createReferralDto.getAppointmentId())).thenReturn(appointment);
        referralService.createReferral(createReferralDto);
        verify(referralRepository).save(any());
        verify(appointmentService).saveAppointment(appointment);
    }

    @Test
    void testUpdateReferral() {
        UpdateReferralDto updateReferralDto = new UpdateReferralDto();
        Referral updatedReferral = new Referral();
        when(referralRepository.findById(1L)).thenReturn(Optional.of(referral));
        when(referralMapper.toUpdateEntity(referral, updateReferralDto)).thenReturn(updatedReferral);
        when(referralRepository.save(updatedReferral)).thenReturn(updatedReferral);
        referralService.updateReferral(1L, updateReferralDto);
        verify(referralRepository).save(updatedReferral);
    }

    @Test
    void testDeleteReferral() {
        when(referralRepository.findById(1L)).thenReturn(java.util.Optional.of(referral));
        referralService.deleteReferral(1L);
        verify(referralRepository).delete(referral);
    }
}
