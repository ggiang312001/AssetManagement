package com.nt.rookies.assets.dtos;

import com.nt.rookies.assets.entities.Assignment;
import com.nt.rookies.assets.entities.ReturnRequestState;
import com.nt.rookies.assets.entities.User;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ReturnRequestDto {
    private int requestId;
    private Assignment assignmentId;
    private String requestNote;
    private LocalDateTime createdAt;
    private UserDto createdBy;
    private LocalDateTime returnedDate;
    private ReturnRequestState state;
    private UserDto acceptedBy;
}
