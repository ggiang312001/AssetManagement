package com.nt.rookies.assets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.rookies.assets.dtos.AssignmentDto;
import com.nt.rookies.assets.dtos.AssignmentRequestDto;
import com.nt.rookies.assets.entities.*;
import com.nt.rookies.assets.mappers.UserMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class AssignmentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public AssignmentControllerTest(ObjectMapper objectMapper, MockMvc mockMvc) {
        super();
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }


    @Test
    @WithMockUser(username = "tuandv", password = "tuandv@01011990", authorities = {"STAFF"})
    public void testUserViewAssignment() throws Exception {
        String requestURL = "/api/v1/assignments";

        mockMvc.perform(get(requestURL)
                        .param("pageNo", "1")
                        .param("pageSize", "5")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "tuandv", password = "tuandv@01011990", authorities = {"STAFF"})
    public void testAssignmentAccepted() throws Exception {

        Asset asset = new Asset("LA001", "laptop Dell HP Probook 450 G4", "RAM 4GB, HDD 500GB, Intel Core i5 gen 5", new Category(), LocalDateTime.now(), new Location(), AssetState.AVAILABLE, LocalDateTime.now());

        Integer assignmentId = 1;

        AssignmentDto assignmentDto = new AssignmentDto(assignmentId, UserMapper.toDto(new User()), UserMapper.toDto(new User()), asset, LocalDate.now(), "note", "WAITING_FOR_ACCEPTANCE", LocalDateTime.now());

        String requestURL = "/api/v1/assignments/1/accept";

        mockMvc.perform(put(requestURL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(assignmentDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "tuandv", password = "tuandv@01011990", authorities = {"STAFF"})
    public void testAssignmentDeclined() throws Exception {

        Asset asset = new Asset("LA001", "laptop Dell HP Probook 450 G4", "RAM 4GB, HDD 500GB, Intel Core i5 gen 5", new Category(), LocalDateTime.now(), new Location(), AssetState.AVAILABLE, LocalDateTime.now());

        Integer assignmentId = 1;

        AssignmentDto assignmentDto = new AssignmentDto(assignmentId, UserMapper.toDto(new User()), UserMapper.toDto(new User()), asset, LocalDate.now(), "note", "WAITING_FOR_ACCEPTANCE", LocalDateTime.now());

        String requestURL = "/api/v1/assignments/1/decline";

        mockMvc.perform(put(requestURL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(assignmentDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "vangdv", password = "$2a$10$ZSlYKBxG/G9TAxc4UK5Irezg6y0QaIMpEkJa1AyxTBs6SBhJ1NBeu", authorities = {"ADMIN"})
    public void testCreateAssignment() throws Exception {
        Integer assignmentId = 1;
     //   Asset asset = new Asset("LA001", "laptop Dell HP Probook 450 G4", "RAM 4GB, HDD 500GB, Intel Core i5 gen 5", new Category(), LocalDateTime.now(), new Location(), AssetState.AVAILABLE);
      //  AssignmentDto assignmentDto = new AssignmentDto(assignmentId, new User(), new User(), asset, LocalDate.now(), "note", "WAITING_FOR_ACCEPTANCE");
        Asset asset = new Asset("LA001","laptop Dell HP Probook 450 G4", "RAM 4GB, HDD 500GB, Intel Core i5 gen 5", new Category(), LocalDateTime.now(), new Location(), AssetState.AVAILABLE, LocalDateTime.now());
        AssignmentDto assignmentDto = new AssignmentDto(assignmentId, UserMapper.toDto(new User()), UserMapper.toDto(new User()), asset, LocalDate.now(), "note", "WAITING_FOR_ACCEPTANCE", LocalDateTime.now());
        String requestURL = "/api/v1/admin/assignments";
        mockMvc.perform(put(requestURL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(assignmentDto)))
                .andExpect(status().isInternalServerError());

    }

    @Test
    @WithMockUser(username = "vangdv", password = "$2a$10$ZSlYKBxG/G9TAxc4UK5Irezg6y0QaIMpEkJa1AyxTBs6SBhJ1NBeu", authorities = {"ADMIN"})
    public void testEditAssignment() throws Exception{
        Integer assignmentId = 2;
        AssignmentRequestDto testEditAssignment = new AssignmentRequestDto(assignmentId, null, "SD0003", "LA003", LocalDate.now(), "Test", AssignmentState.WAITING_FOR_ACCEPTANCE);
        String requestUrl = "/api/v1/admin/assignments/2";
        mockMvc.perform(put(requestUrl)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testEditAssignment)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "vangdv", password = "$2a$10$ZSlYKBxG/G9TAxc4UK5Irezg6y0QaIMpEkJa1AyxTBs6SBhJ1NBeu", authorities = {"ADMIN"})
    public void testDeleteAssignment() throws Exception{
        Integer assignmentId = 2;
        AssignmentRequestDto testEditAssignment = new AssignmentRequestDto(assignmentId, null, "SD0003", "LA003", LocalDate.now(), "Test", AssignmentState.WAITING_FOR_ACCEPTANCE);
        String requestUrl = "/api/v1/admin/assignments/2";
        mockMvc.perform(MockMvcRequestBuilders.delete(requestUrl)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(testEditAssignment)))
                .andExpect(status().is2xxSuccessful());
    }
}