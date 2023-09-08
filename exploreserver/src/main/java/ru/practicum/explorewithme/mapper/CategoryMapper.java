package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.dto.CategoryDto;
import ru.practicum.explorewithme.model.Category;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public interface CategoryMapper {
    CategoryDto CategoryToCategoryDto(Category category);

    Category CategoryDtoToCategory(CategoryDto categoryDto);

    Category toCategoryFromLong(Long id);
}
