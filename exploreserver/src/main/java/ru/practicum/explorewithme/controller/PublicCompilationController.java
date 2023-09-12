package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.CompilationDto;
import ru.practicum.explorewithme.service.CompilationService;

import java.util.Collection;

import static ru.practicum.explorewithme.utils.Constants.PAGE_FROM_DEFAULT;
import static ru.practicum.explorewithme.utils.Constants.PAGE_SIZE_DEFAULT;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/compilations")
@Validated
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public Collection<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                      @RequestParam(defaultValue = PAGE_FROM_DEFAULT) Integer from,
                                                      @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) Integer size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        return compilationService.getCompilationById(compId);
    }
}
