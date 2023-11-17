package com.nt.rookies.assets.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDto {
    private String sortBy = "category";
    private String sortDir = "asc";
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
