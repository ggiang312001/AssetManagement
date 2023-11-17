package com.nt.rookies.assets.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LocationDto {
    private int locationId;

    private String city;
}
