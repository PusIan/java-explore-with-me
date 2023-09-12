package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.CategoryDto;
import ru.practicum.explorewithme.service.CategoryService;

import java.util.Collection;

import static ru.practicum.explorewithme.utils.Constants.PAGE_FROM_DEFAULT;
import static ru.practicum.explorewithme.utils.Constants.PAGE_SIZE_DEFAULT;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryDto> getAllCategories(@RequestParam(defaultValue = PAGE_FROM_DEFAULT) Integer from,
                                                    @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) Integer size) {
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        return categoryService.getCategory(catId);
    }
}
