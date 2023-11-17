package com.nt.rookies.assets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.rookies.assets.dtos.UserRequestDto;
import com.nt.rookies.assets.entities.Gender;
import com.nt.rookies.assets.entities.Role;
import com.nt.rookies.assets.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class UserControllerTest {
    private final static String API_USER = "/api/v1/admin/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        super();
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @WithMockUser(username = "vangdv", password = "12345678", authorities = {"ADMIN"})
    public void testAdminViewUser() throws Exception {
        String requestURL = "/api/v1/admin/users";

        mockMvc.perform(get(requestURL)
                        .param("pageNo", "1")
                        .param("pageSize", "10")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "vangdv", password = "12345678", authorities = {"ADMIN"})
    public void testCreateNewUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("chung", "bui van", "1990-01-01", Gender.MALE, "2022-11-30", Role.STAFF);

        mockMvc.perform(post(API_USER)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "vangdv", password = "12345678", authorities = {"ADMIN"})
    public void testEditUser() throws Exception {
        UserRequestDto userRequestDto = new UserRequestDto("chung", "bui van", "1990-01-01", Gender.MALE, "2022-11-30", Role.STAFF);
        String staffCode = "SD0001";

        mockMvc.perform(patch(API_USER + "/" + staffCode)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "vangdv", password = "12345678", authorities = {"ADMIN"})
    public void testDisableUser() throws Exception {
        String staffCode = "SD0001";

        mockMvc.perform(patch(API_USER + "/" + staffCode + "/disable")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

}
