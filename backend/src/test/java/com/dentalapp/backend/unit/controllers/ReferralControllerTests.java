package com.dentalapp.backend.unit.controllers;

import com.dentalapp.backend.controllers.ReferralController;
import com.dentalapp.backend.model.referral.dtos.CreateReferralDto;
import com.dentalapp.backend.model.referral.dtos.CreateReferralsDto;
import com.dentalapp.backend.model.referral.dtos.UpdateReferralDto;
import com.dentalapp.backend.model.referral.entity.Referral;
import com.dentalapp.backend.services.ReferralService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReferralControllerTests {
    @Mock
    private ReferralService referralService;

    @InjectMocks
    private ReferralController referralController;

    private Referral referral;

    private Validator validator;

    CreateReferralsDto createReferralsDto;

    CreateReferralDto createReferralDto;

    UpdateReferralDto updateReferralDto;

    @BeforeEach
    public void setUp() {
        referral = new Referral();

        createReferralsDto = new CreateReferralsDto();
        createReferralDto = new CreateReferralDto();
        createReferralDto.setProcedureName("Procedure Name");
        createReferralDto.setProcedureDescription("Procedure Description");
        createReferralDto.setDoctorName("Doctor Name");
        createReferralDto.setClinicName("Clinic Name");
        createReferralDto.setClinicAddress("Clinic Address");
        createReferralsDto.setCreateReferralDtoList(List.of(createReferralDto));
        createReferralsDto.setAppointmentId(1L);

        UpdateReferralDto updateReferralDto = new UpdateReferralDto();
        updateReferralDto.setProcedureName("Procedure Name");
        updateReferralDto.setProcedureDescription("Procedure Description");
        updateReferralDto.setDoctorName("Doctor Name");
        updateReferralDto.setClinicName("Clinic Name");
        updateReferralDto.setClinicAddress("Clinic Address");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidation() {
        createReferralsDto.setAppointmentId(null);
        createReferralsDto.setCreateReferralDtoList(null);
        Assertions.assertEquals(2, validator.validate(createReferralsDto).size());
        createReferralsDto.setCreateReferralDtoList(List.of(createReferralDto));
        Assertions.assertEquals(1, validator.validate(createReferralsDto).size());

        createReferralDto.setProcedureDescription(null);
        createReferralDto.setProcedureName(null);
        createReferralDto.setClinicName(null);
        Assertions.assertEquals(3, validator.validate(createReferralDto).size());
    }

    @Test
    public void testGetAllReferrals() {
        when(referralService.findAll()).thenReturn(List.of(referral));
        ResponseEntity<List<Referral>> response = referralController.getAllReferrals();
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    public void testGetReferralById() {
        when(referralService.findById(1L)).thenReturn(referral);
        ResponseEntity<Referral> response = referralController.getReferralById(1L);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals(referral, response.getBody());
    }

    @Test
    public void testCreateReferral() {
        ResponseEntity<String> response = referralController.createReferral(createReferralsDto);
        Assertions.assertEquals(201, response.getStatusCode().value());
        Assertions.assertEquals("Referral created successfully", response.getBody());
    }

    @Test
    public void testUpdateReferral() {
        ResponseEntity<String> response = referralController.updateReferral(1L, updateReferralDto);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Referral updated successfully", response.getBody());
    }

    @Test
    public void testDeleteReferral() {
        ResponseEntity<String> response = referralController.deleteReferral(1L);
        Assertions.assertEquals(200, response.getStatusCode().value());
        Assertions.assertEquals("Referral deleted successfully", response.getBody());
    }
}
