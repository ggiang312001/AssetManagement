package com.nt.rookies.assets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ReturnRequestControllerTest {
    private final static String API_RETURN_REQUEST = "/api/v1/admin/return-request";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public ReturnRequestControllerTest(ObjectMapper objectMapper, MockMvc mockMvc) {
        super();
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }


    @Test
    @WithMockUser(username = "vangdv", password = "12345678", authorities = {"ADMIN"})
    public void testGetReturnRequests() throws Exception {
        mockMvc.perform(get(API_RETURN_REQUEST)
                        .param("pageNo", "1")
                        .param("pageSize", "5")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}
