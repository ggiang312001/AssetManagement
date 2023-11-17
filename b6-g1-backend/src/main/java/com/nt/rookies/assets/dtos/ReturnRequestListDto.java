package com.nt.rookies.assets.dtos;

import com.nt.rookies.assets.entities.ReturnRequestState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter @Setter
public class ReturnRequestListDto {

    private String searchTerm;
    private String sortBy = "requestId";
    private String sortDir = "asc";
    private String returnedDate;
    private String state;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
