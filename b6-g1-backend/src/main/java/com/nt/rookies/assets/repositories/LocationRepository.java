package com.nt.rookies.assets.repositories;

import com.nt.rookies.assets.entities.Location;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends PagingAndSortingRepository<Location, Integer> {
    Optional<Location> findByLocationId(Integer locationId);
}
