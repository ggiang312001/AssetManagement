package com.nt.rookies.assets.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.rookies.assets.dtos.ChangePasswordDto;
import com.nt.rookies.assets.dtos.LoginDto;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class AuthControllerTests {

    private final static String API_LOGIN_URL = "/api/v1/auth/login";
    private final static String API_CHANGE_PASSWORD_URL = "/api/v1/auth/change-password";
    private String username = "vangdv";
    private String nonExistUsername = "chungbui";
    private String correctPassword = "12345678";
    private String wrongPassword = "abcxyzfgh";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public AuthControllerTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        super();
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testLoginFailedWithWrongPassword() throws Exception {

        LoginDto loginDto = new LoginDto(username, wrongPassword);

        mockMvc.perform(post(API_LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad credentials"));
    }

    @Test
    public void testLoginFailedWithNonExistUsername() throws Exception {

        LoginDto loginDto = new LoginDto(nonExistUsername, correctPassword);

        mockMvc.perform(post(API_LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User with username: " + nonExistUsername + " not found"));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginDto loginDto = new LoginDto(username, correctPassword);

        mockMvc.perform(post(API_LOGIN_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "vangdv", password = "12345678", roles = {"ADMIN"})
    public void testChangePassword() throws Exception {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto("12345678", "87654321");

        mockMvc.perform(patch(API_CHANGE_PASSWORD_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(changePasswordDto)))
                .andExpect(status().isOk());
    }

}
