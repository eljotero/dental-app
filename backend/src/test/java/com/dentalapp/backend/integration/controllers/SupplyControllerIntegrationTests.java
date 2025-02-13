package com.dentalapp.backend.integration.controllers;

import com.dentalapp.backend.BackendApplication;
import com.dentalapp.backend.model.supplies.dtos.CreateSupplyDto;
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
public class SupplyControllerIntegrationTests {

    private final MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    public SupplyControllerIntegrationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setUp() {
        baseURI = "https://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    public void testGetSupplies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/supply/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void testGetSupplyById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/supply/51"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supplyId").value(51));
    }

    @Test
    public void testAddSupply() throws Exception {
        CreateSupplyDto createSupplyDto = new CreateSupplyDto();
        createSupplyDto.setLink("https://www.example.com");
        createSupplyDto.setName("Example Supply");
        createSupplyDto.setQuantity(10D);
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());
        String json = ow.writerWithDefaultPrettyPrinter().writeValueAsString(createSupplyDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/supply/add")
                .contentType("application/json")
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteSupply() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/supply/50"))
                .andExpect(status().isOk());
    }
}
