package com.nt.rookies.assets.mappers;

import com.nt.rookies.assets.dtos.CategoryDto;
import com.nt.rookies.assets.entities.Category;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static CategoryDto toDto(Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(category.getCategoryId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static Category toEntity(CategoryDto categoryDto){
        Category category = new Category();
        category.setCategoryId(categoryDto.getCategoryId());
        category.setName(categoryDto.getName());
        return category;
    }

    public static List<CategoryDto> toDtoList(List<Category> entities){
        return entities.stream().map(CategoryMapper::toDto).collect(Collectors.toList());
    }

    public static List<CategoryDto> toDtoList(Iterable<Category> entities){
        List<CategoryDto> categoryDtos = new LinkedList<>();
        entities.forEach(e -> categoryDtos.add(toDto(e)));
        return categoryDtos;
    }
 }
