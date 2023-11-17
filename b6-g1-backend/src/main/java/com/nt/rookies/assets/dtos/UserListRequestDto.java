package com.nt.rookies.assets.dtos;

import com.nt.rookies.assets.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserListRequestDto {
    private String searchTerm = "";
    private String[] sort = {"updatedAt,desc"};
    private Role role = null;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}

