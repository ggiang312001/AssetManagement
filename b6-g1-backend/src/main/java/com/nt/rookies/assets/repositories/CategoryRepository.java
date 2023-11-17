package com.nt.rookies.assets.repositories;

import com.nt.rookies.assets.entities.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Optional<Category> findByCategoryId(int categoryId);

    @Query("select c from Category c where c.categoryId = ?1")
    Category findById(int categoryId);
}
