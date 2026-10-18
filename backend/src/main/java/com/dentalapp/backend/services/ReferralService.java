package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.referral.dtos.CreateReferralDto;
import com.dentalapp.backend.model.referral.dtos.GetReferralDto;
import com.dentalapp.backend.model.referral.dtos.ReferralMapper;
import com.dentalapp.backend.model.referral.dtos.UpdateReferralDto;
import com.dentalapp.backend.model.referral.dtos.ReferralPageResponse;
import com.dentalapp.backend.model.referral.dtos.ReferralQueryParams;
import com.dentalapp.backend.model.referral.entity.Referral;
import com.dentalapp.backend.model.referral.exception.ReferralNotFoundException;
import com.dentalapp.backend.model.referral.repository.ReferralRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS)
public class ReferralService {

    private final ReferralRepository referralRepository;

    private final AppointmentService appointmentService;

    private final ReferralMapper referralMapper;

    public ReferralPageResponse<GetReferralDto> findAll(ReferralQueryParams params) {
        return ReferralPageResponse.from(referralRepository.findAll(params.toPageable()).map(referralMapper::toGetReferralDto));
    }

    public GetReferralDto findById(Long id) {
        return referralRepository.findById(id).map(referralMapper::toGetReferralDto).orElseThrow(ReferralNotFoundException::new);
    }

    public ReferralPageResponse<GetReferralDto> findAllByPatient(String patientEmail, ReferralQueryParams params) {
        return ReferralPageResponse.from(referralRepository.findAllByPatientEmail(patientEmail, params.toPageable()).map(referralMapper::toGetReferralDto));
    }

    public ReferralPageResponse<GetReferralDto> findAllByDoctor(String doctorEmail, ReferralQueryParams params) {
        return ReferralPageResponse.from(referralRepository.findAllByDoctorEmail(doctorEmail, params.toPageable()).map(referralMapper::toGetReferralDto));
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
        Referral referral = referralRepository.findById(referralId).orElseThrow(ReferralNotFoundException::new);
        Referral updatedReferral = referralMapper.toUpdateEntity(referral, updateReferralDto);
        referralRepository.save(updatedReferral);
    }

    @Transactional
    public void deleteReferral(Long referralId) {
        Referral referral = referralRepository.findById(referralId).orElseThrow(ReferralNotFoundException::new);
        referralRepository.delete(referral);
    }
}
