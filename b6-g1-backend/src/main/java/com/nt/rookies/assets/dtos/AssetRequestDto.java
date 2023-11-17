package com.nt.rookies.assets.dtos;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.nt.rookies.assets.entities.AssetState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetRequestDto {
    private String assetId;

    private String name;

    private String specification;

    private String category;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate installedDate; 

    private Integer locationId;

    private AssetState state;
}
