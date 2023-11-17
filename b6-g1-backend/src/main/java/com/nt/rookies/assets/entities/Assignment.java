package com.nt.rookies.assets.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "assignment")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private int assignmentId;

    @ManyToOne
    @JoinColumn(name = "assigner")
    private User assigner;

    @ManyToOne
    @JoinColumn(name = "assignee")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "assetId")
    private Asset assetId;

    @Column(name = "assign_date")
    @NotNull
    private LocalDate assignDate;

    @Column(name = "assign_note")
    private String assignNote;

    @Column(name = "state")
    @NotNull
    @Enumerated(EnumType.STRING)
    private AssignmentState state;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
