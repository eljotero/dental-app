package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.referral.dtos.CreateReferralsDto;
import com.dentalapp.backend.model.referral.dtos.ReferralMapper;
import com.dentalapp.backend.model.referral.dtos.UpdateReferralDto;
import com.dentalapp.backend.model.referral.entity.Referral;
import com.dentalapp.backend.model.referral.exception.ReferralNotFoundException;
import com.dentalapp.backend.model.referral.repository.ReferralRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReferralService {

    private final ReferralRepository referralRepository;

    private final AppointmentService appointmentService;

    public ReferralService(ReferralRepository referralRepository, AppointmentService appointmentService) {
        this.referralRepository = referralRepository;
        this.appointmentService = appointmentService;
    }

    public List<Referral> findAll() {
        return referralRepository.findAll();
    }

    public Referral findById(Long id) {
        return referralRepository.findById(id).orElseThrow(ReferralNotFoundException::new);
    }

    @Transactional
    public void createReferral(CreateReferralsDto createReferralsDto) {
        Appointment appointment = appointmentService.getAppointmentById(createReferralsDto.getAppointmentId());
        List<Referral> referrals = createReferralsDto.getCreateReferralDtoList().stream().map(dto -> ReferralMapper.toEntity(dto, appointment)).collect(Collectors.toList());
        referralRepository.saveAll(referrals);
        appointment.setReferrals(referrals);
        appointmentService.saveAppointment(appointment);
    }

    @Transactional
    public void updateReferral(Long referralId, UpdateReferralDto updateReferralDto) {
        Referral referral = referralRepository.findById(referralId).orElseThrow();
        Referral updatedReferral = ReferralMapper.toUpdateEntity(referral, updateReferralDto);
        referralRepository.save(updatedReferral);
    }

    @Transactional
    public void deleteReferral(Long referralId) {
        referralRepository.deleteById(referralId);
    }
}
