package com.nt.rookies.assets.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private int categoryId;

    private String name;
}
