package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.CategoryDto;
import com.nt.rookies.assets.mappers.CategoryMapper;
import com.nt.rookies.assets.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    private CategoryRepository categoryRepositoryrepository;

    public CategoryService(CategoryRepository repository) {
        categoryRepositoryrepository = Objects.requireNonNull(repository);
    }

    public List<CategoryDto> getAll() {
        return CategoryMapper.toDtoList(categoryRepositoryrepository.findAll());
    }
}
