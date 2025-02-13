package com.dentalapp.backend.integration.controllers;

import com.dentalapp.backend.BackendApplication;
import com.dentalapp.backend.model.user.dtos.CreateUserDto;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BackendApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {

    private final MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    public UserControllerIntegrationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    public void setUp() {
        baseURI = "https://localhost";
        RestAssured.port = port;
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"DOCTOR"})
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"DOCTOR"})
    public void testGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(50));
    }

    @Test
    public void testRegisterUser() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setFirstName("John");
        createUserDto.setLastName("Doe");
        createUserDto.setEmail("test12@mail.com");
        createUserDto.setPassword("password123");
        createUserDto.setPhoneNumber("123456789");
        createUserDto.setSex(true);
        createUserDto.setPersonalIdNumber("12345678901");
        createUserDto.setCountry("Country");
        createUserDto.setCity("City");
        createUserDto.setAddressLine("Address");
        createUserDto.setZipCode("12345");
        createUserDto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        createUserDto.setLanguage("en");
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());
        String json = ow.writerWithDefaultPrettyPrinter().writeValueAsString(createUserDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterUserAlreadyExists() throws Exception {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setFirstName("John");
        createUserDto.setLastName("Doe");
        createUserDto.setEmail("test12@mail.com");
        createUserDto.setPassword("password123");
        createUserDto.setPhoneNumber("123456789");
        createUserDto.setSex(true);
        createUserDto.setPersonalIdNumber("12345678901");
        createUserDto.setCountry("Country");
        createUserDto.setCity("City");
        createUserDto.setAddressLine("Address");
        createUserDto.setZipCode("12345");
        createUserDto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        createUserDto.setLanguage("en");
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());
        String json = ow.writerWithDefaultPrettyPrinter().writeValueAsString(createUserDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isConflict());
    }
}
