package com.nt.rookies.assets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.rookies.assets.dtos.AssetDto;
import com.nt.rookies.assets.dtos.CategoryDto;
import com.nt.rookies.assets.dtos.LocationDto;
import com.nt.rookies.assets.entities.AssetState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class AssetControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public AssetControllerTest(ObjectMapper objectMapper, MockMvc mockMvc) {
        super();
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    @Test
    @WithMockUser(username = "vietvh", password = "vietvh@01011990", authorities = {"ADMIN"})
    public void testViewBy() throws Exception {
        String requestURL = "/api/v1/admin/assets";
        mockMvc.perform(get(requestURL)
                        .param("searchTerm", "")
                        .param("sortBy", "updatedAt")
                        .param("sortDir", "asc")
                        .param("cateFill","0")
                        .param("stateFill", "AVAILABLE")
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "vietvh", password = "vietvh@01011990", authorities = {"ADMIN"})
    public void testGetByCode() throws Exception {
        String requestURL = "/api/v1/admin/assets/LA001";
        AssetDto assetDto = new AssetDto();
        mockMvc.perform(get(requestURL)
                        .content(objectMapper.writeValueAsString(assetDto))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "vietvh", password = "vietvh@01011990", authorities = {"ADMIN"})
    public void testEditAsset() throws Exception {
        String requestURL = "/api/v1/admin/assets/LA001";
        AssetDto assetDto = new AssetDto("LA001", "Asus Gaming", "Laptop Gaming ProVip", new CategoryDto(), LocalDateTime.now(), new LocationDto(), AssetState.AVAILABLE, LocalDateTime.now());

        mockMvc.perform(put(requestURL)
                        .content(objectMapper.writeValueAsString(assetDto))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "vietvh", password = "vietvh@01011990", authorities = {"ADMIN"})
    public void testDeleteAsset() throws Exception {
        String requestURL = "/api/v1/admin/assets/LA001";
        AssetDto assetDto = new AssetDto("LA001", "Asus Gaming", "Laptop Gaming ProVip", new CategoryDto(), LocalDateTime.now(), new LocationDto(), AssetState.AVAILABLE, LocalDateTime.now());

        mockMvc.perform(patch(requestURL)
                        .content(objectMapper.writeValueAsString(assetDto))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "vietvh", password = "vietvh@01011990", authorities = {"ADMIN"})
    public void testCheckHistoryAsset() throws Exception {
        String requestURL = "/api/v1/admin/assets/check-history/LA001";
        mockMvc.perform(get(requestURL)
                        .content(objectMapper.toString())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}