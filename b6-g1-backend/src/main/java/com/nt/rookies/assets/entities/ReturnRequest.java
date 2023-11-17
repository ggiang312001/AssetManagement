package com.nt.rookies.assets.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "return_request")
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ReturnRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;

    @OneToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignmentId;

    @Column(name = "request_note")
    private String requestNote;

    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "accepted_by")
    private User acceptedBy;

    @Column(name = "returned_date")
    private LocalDateTime returnedDate;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ReturnRequestState state;
}
