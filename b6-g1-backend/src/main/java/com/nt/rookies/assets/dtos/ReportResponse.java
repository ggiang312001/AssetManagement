package com.nt.rookies.assets.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private List<ReportResponseDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
}
