package com.nt.rookies.assets.dtos;

import com.nt.rookies.assets.entities.Asset;
// import com.nt.rookies.assets.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class AssignmentDto {
    private int assignmentId;
    private UserDto assigner;
    private UserDto assignee;
    private Asset assetId;
    @NotEmpty(message = "assignDate is required")
    private LocalDate assignDate;
    private String assignNote;
    @NotEmpty(message = "state is required")
    private String state;
    private LocalDateTime updatedAt;
}