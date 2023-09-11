package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.CategoryDto;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CategoryMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.repository.CategoryRepository;
import ru.practicum.explorewithme.utils.Utilities;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return categoryMapper.categoryToCategoryDto(
                categoryRepository.save(categoryMapper.categoryDtoToCategory(categoryDto)));
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        return categoryMapper.categoryToCategoryDto(
                categoryRepository.save(categoryMapper.categoryDtoToCategory(categoryDto)));
    }

    @Override
    public Collection<CategoryDto> getAllCategories(Integer from, Integer size) {
        Pageable pageable = Utilities.getPageable(from, size, Sort.by("id").ascending());
        return categoryRepository.findAll(pageable).stream().map(categoryMapper::categoryToCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        return categoryRepository.findById(catId).map(categoryMapper::categoryToCategoryDto)
                .orElseThrow(() -> new NotFoundException(catId, Category.class));
    }
}
