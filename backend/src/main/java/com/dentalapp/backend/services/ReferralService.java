package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.referral.dtos.CreateReferralDto;
import com.dentalapp.backend.model.referral.dtos.GetReferralDto;
import com.dentalapp.backend.model.referral.dtos.ReferralMapper;
import com.dentalapp.backend.model.referral.dtos.UpdateReferralDto;
import com.dentalapp.backend.model.referral.entity.Referral;
import com.dentalapp.backend.model.referral.exception.ReferralNotFoundException;
import com.dentalapp.backend.model.referral.repository.ReferralRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ReferralService {

    private final ReferralRepository referralRepository;

    private final AppointmentService appointmentService;

    private final ReferralMapper referralMapper;

    public List<GetReferralDto> findAll() {
        return referralRepository.findAll().stream().map(referralMapper::toGetReferralDto).toList();
    }

    public GetReferralDto findById(Long id) {
        return referralRepository.findById(id).map(referralMapper::toGetReferralDto).orElseThrow(() -> new ReferralNotFoundException("Referral not found"));
    }

    public List<GetReferralDto> findAllByPatient(String patientEmail) {
        return referralRepository.findAllByPatientEmail(patientEmail).stream().map(referralMapper::toGetReferralDto).toList();
    }

    public List<GetReferralDto> findAllByDoctor(String doctorEmail) {
        return referralRepository.findAllByDoctorEmail(doctorEmail).stream().map(referralMapper::toGetReferralDto).toList();
    }

    @Transactional
    public void createReferral(CreateReferralDto createReferralDto) {
        Appointment appointment = appointmentService.getAppointmentById(createReferralDto.getAppointmentId());
        Referral referral = referralMapper.toEntity(createReferralDto, appointment);
        referralRepository.save(referral);
        appointment.getReferrals().add(referral);
        appointmentService.saveAppointment(appointment);
    }

    @Transactional
    public void updateReferral(Long referralId, UpdateReferralDto updateReferralDto) {
        Referral referral = referralRepository.findById(referralId).orElseThrow(() -> new ReferralNotFoundException("Referral not found"));
        Referral updatedReferral = referralMapper.toUpdateEntity(referral, updateReferralDto);
        referralRepository.save(updatedReferral);
    }

    @Transactional
    public void deleteReferral(Long referralId) {
        Referral referral = referralRepository.findById(referralId).orElseThrow(() -> new ReferralNotFoundException("Refferal not found"));
        referral.onDelete();
        referralRepository.save(referral);
    }
}
