package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.CategoryDto;

import java.util.Collection;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto patchCategory(CategoryDto categoryDto);

    Collection<CategoryDto> getAllCategories(Integer from, Integer size);

    CategoryDto getCategory(Long catId);
}
