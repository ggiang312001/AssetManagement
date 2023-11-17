package com.nt.rookies.assets.entities;

import com.nt.rookies.assets.utils.StringPrefixedSequenceIdGenerator;
import lombok.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    @Column(name = "staff_code")
    @NotEmpty(message = "staffCode is required")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_code_generator")
    @GenericGenerator(
            name = "staff_code_generator",
            strategy = "com.nt.rookies.assets.utils.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INITIAL_PARAM, value="20"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "SD"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%04d") })
    private String staffCode;

    @Column(name = "username")
    @NotEmpty(message = "username is required")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "password is required")
    private String password;

    @Column(name = "first_name")
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    private String lastName;

    @Column(name = "birthdate")
    @NotNull
    private LocalDate birthDate;

    @Column(name = "gender")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "email")
    private String email;

    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    @NotNull
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "active")
    @NotNull
    private Boolean active;

    @Column(name = "first_login")
    @NotNull
    private Boolean firstLogin ;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location locationId;

    public User(String staffCode, String username, String password, Role role) {
        this.staffCode = staffCode;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String staffCode, String username, String password, Role role, Boolean firstLogin) {
        this.staffCode = staffCode;
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstLogin = firstLogin;
    }

    public User(String staffCode, String username, String password, String firstName, String lastName, LocalDate birthDate, Gender gender, String email, LocalDateTime createdAt, String createdBy, Boolean active, Location locationId, Role role, Boolean firstLogin) {
        this.staffCode = staffCode;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.active = active;
        this.firstLogin = firstLogin;
        this.role = role;
        this.locationId = locationId;
    }

    public User(String username, String password, String firstName, String lastName, LocalDate birthDate, Gender gender, String email, LocalDateTime createdAt, String createdBy, Boolean active, Location locationId, Role role, Boolean firstLogin) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.active = active;
        this.firstLogin = firstLogin;
        this.role = role;
        this.locationId = locationId;
    }

    public User(String username, String password, String firstName, String lastName, LocalDate birthDate, Gender gender, String email, LocalDateTime createdAt, String createdBy, Boolean active, Location locationId, Role role, Boolean firstLogin, LocalDateTime updatedAt) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.active = active;
        this.firstLogin = firstLogin;
        this.role = role;
        this.locationId = locationId;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "staffCode='" + staffCode + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
