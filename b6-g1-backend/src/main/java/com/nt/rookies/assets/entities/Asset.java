package com.nt.rookies.assets.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Asset {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    @NotEmpty(message = "assetCode is required")
    private String assetId;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "specification")
    @NotNull
    private String specification;

    @ManyToOne
    @JoinColumn(name="category")

    private Category category;

    @Column(name = "installed_date")

    private LocalDateTime installedDate;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location locationId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private AssetState state;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}