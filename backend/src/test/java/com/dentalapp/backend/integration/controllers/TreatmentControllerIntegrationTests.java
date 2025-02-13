package com.dentalapp.backend.integration.controllers;

import com.dentalapp.backend.BackendApplication;
import com.dentalapp.backend.model.treatment.dtos.CreateTreatmentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BackendApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TreatmentControllerIntegrationTests {

    private final MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    public TreatmentControllerIntegrationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setUp() {
        baseURI = "https://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void testGetAllTreatments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/services/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testGetTreatmentById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/services/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.treatmentId").value(50));
    }

    @Test
    public void testAddTreatment() throws Exception {
        CreateTreatmentDto createTreatmentDto = new CreateTreatmentDto();
        createTreatmentDto.setTreatmentName("Test treatment4");
        createTreatmentDto.setTreatmentDescription("Test description4");
        createTreatmentDto.setTreatmentPrice(100L);
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());
        String json = ow.writerWithDefaultPrettyPrinter().writeValueAsString(createTreatmentDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/services/create")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteTreatment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/services/52"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Treatment deleted successfully"));
    }
}
