package com.nt.rookies.assets.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nt.rookies.assets.entities.Gender;
import com.nt.rookies.assets.entities.Location;
import com.nt.rookies.assets.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty(message = "staffCode is required")
    private String staffCode;

    @NotEmpty(message = "username is required")
    private String username;

    @JsonIgnore
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private Gender gender;

    private String email;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    @NotNull
    private Boolean active;

    @NotNull
    private Boolean firstLogin ;

    private Role role;

    private Location locationId;

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "staffCode='" + staffCode + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
