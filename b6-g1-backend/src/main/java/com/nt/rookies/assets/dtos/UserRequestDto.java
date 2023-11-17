package com.nt.rookies.assets.dtos;

import com.nt.rookies.assets.entities.Gender;
import com.nt.rookies.assets.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotNull
    @Size(min = 1, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    private String lastName;

    @NotNull
    private String birthDate;

    @NotNull
    private Gender gender;

    @NotNull
    private String createdAt;

    @NotNull
    private Role role;

}
