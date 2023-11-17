package com.nt.rookies.assets.repositories;

import com.nt.rookies.assets.entities.ReturnRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnRequestRepository extends CrudRepository<ReturnRequest, Integer> {
}
