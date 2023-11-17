package com.nt.rookies.assets.dtos;

import java.time.LocalDateTime;

import com.nt.rookies.assets.entities.AssetState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetDto {
    private String assetId;

    private String name;

    private String specification;

    private CategoryDto category;

    private LocalDateTime installedDate;

    private LocationDto locationId;

    private AssetState state;

    private LocalDateTime updatedAt;

}
