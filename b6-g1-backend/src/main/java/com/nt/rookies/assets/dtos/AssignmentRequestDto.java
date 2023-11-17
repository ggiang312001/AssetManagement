package com.nt.rookies.assets.dtos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.nt.rookies.assets.entities.AssignmentState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequestDto {
    private Integer assignmentId;
    private String assigner;
    private String assignee;
    private String assetId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate assignDate; 
    private String assignNote;
    private AssignmentState state;
}
