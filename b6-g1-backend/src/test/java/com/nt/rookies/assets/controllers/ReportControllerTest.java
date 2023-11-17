package com.nt.rookies.assets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.rookies.assets.dtos.ReportRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ReportControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public ReportControllerTest(ObjectMapper objectMapper, MockMvc mockMvc) {
        super();
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    @Test
    @WithMockUser(username = "vangdv", password = "vangdv@01011990", authorities = {"ADMIN"})
    public void testExportReport() throws Exception {
        ReportRequestDto reportRequestDto = new ReportRequestDto("category", "asc", 1, 10);
        String requestURL = "/api/v1/admin/report/export";
        mockMvc.perform(get(requestURL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reportRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "vangdv", password = "vangdv@01011990", authorities = {"ADMIN"})
    public void testReport() throws Exception {
        ReportRequestDto reportRequestDto = new ReportRequestDto("category", "asc", 1, 10);
        String requestURL = "/api/v1/admin/report";
        mockMvc.perform(get(requestURL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reportRequestDto)))
                .andExpect(status().isOk());

    }
}
