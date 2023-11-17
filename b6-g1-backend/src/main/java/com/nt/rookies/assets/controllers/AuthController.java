package com.nt.rookies.assets.controllers;

import com.nt.rookies.assets.dtos.ChangePasswordDto;
import com.nt.rookies.assets.dtos.JwtAuthResponse;
import com.nt.rookies.assets.dtos.LoginDto;
import com.nt.rookies.assets.dtos.UserDto;
import com.nt.rookies.assets.securities.JwtTokenProvider;
import com.nt.rookies.assets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {

    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService) {
        this.userService = Objects.requireNonNull(userService);
    }

    /**
     * Login
     * @param loginDto includes username and password
     * @return JWT access token, token type, role, isFirstLogin
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody @Valid LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get user info
        UserDto userDto = userService.getByUsername(loginDto.getUsername());
        String role = userDto.getRole().name();
        Boolean isFirstLogin = userDto.getFirstLogin();

        // Get token from tokenProvider
        String token = jwtTokenProvider.generateToken(authentication);

        return new ResponseEntity<>(new JwtAuthResponse(role, isFirstLogin, token), HttpStatus.OK);
    }

    /**
     * Change Password
     * @param changePasswordDto includes old password and new password
     * @param principal Principal of Spring Security which has user details (username, roles)
     * @return userDto user information
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/change-password")
    public ResponseEntity<UserDto> changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto, Principal principal){
        String username = principal.getName();
        UserDto userDto = userService.changePassword(changePasswordDto, username);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


}
