package com.nt.rookies.assets.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {
    private CategoryDto categoryDto;
    private Integer total;
    private Integer assigned;
    private Integer available;
    private Integer notAvailable;
    private Integer waitingForRecycling;
    private Integer recycled;
}
