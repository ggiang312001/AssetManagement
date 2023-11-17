package com.nt.rookies.assets.dtos;

import com.nt.rookies.assets.entities.AssetState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetListRequestDto {
    private String searchTerm = "";
    private String sortBy = "updatedAt";
    private String sortDir = "asc";
    private Integer cateFill = 0;
    private AssetState stateFill;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
