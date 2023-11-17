
package com.nt.rookies.assets.mappers;

import com.nt.rookies.assets.dtos.LocationDto;
import com.nt.rookies.assets.entities.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class LocationMapper {

    public static LocationDto toDto(Location location){
        LocationDto locationDto = new LocationDto();
        locationDto.setLocationId(location.getLocationId());
        locationDto.setCity(location.getCity());
        return locationDto;
    }

    public static Location toEntity(LocationDto locationDto){
        Location entity = new Location();
        entity.setLocationId(locationDto.getLocationId());
        entity.setCity(locationDto.getCity());
        return entity;
    }

    public static List<LocationDto> toDtoList(List<Location> entities){
        return entities.stream().map(LocationMapper::toDto).collect(Collectors.toList());
    }

    public static List<LocationDto> toDtoList(Iterable<Location> entities){
        List<LocationDto> locationDtos = new LinkedList<>();
        entities.forEach(e -> locationDtos.add(toDto(e)));
        return locationDtos;
    }
}
