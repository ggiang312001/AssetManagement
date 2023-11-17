package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.LocationDto;
import com.nt.rookies.assets.mappers.LocationMapper;
import com.nt.rookies.assets.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class LocationService {
    private LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = Objects.requireNonNull(locationRepository);
    }

    public List<LocationDto> getAll() {
        return LocationMapper.toDtoList(locationRepository.findAll());
    }
}
