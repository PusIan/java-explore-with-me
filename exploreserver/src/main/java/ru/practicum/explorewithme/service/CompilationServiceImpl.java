package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.CompilationDto;
import ru.practicum.explorewithme.dto.NewCompilationDto;
import ru.practicum.explorewithme.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CompilationMapper;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.utils.Utilities;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        return compilationMapper.compilationToCompilationDto(
                compilationRepository.save(compilationMapper.newCompilationDtoToCompilation(newCompilationDto)));
    }

    @Override
    public CompilationDto patchCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(compId, Compilation.class)
        );
        Optional.ofNullable(updateCompilationRequest.getEvents()).ifPresent(x -> compilation.setEvents(eventMapper.idsToEvents(x)));
        Optional.ofNullable(updateCompilationRequest.getPinned()).ifPresent(compilation::setPinned);
        Optional.ofNullable(updateCompilationRequest.getTitle()).ifPresent(compilation::setTitle);
        return compilationMapper.compilationToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable page = Utilities.getPageable(from, size, Sort.by("id").ascending());
        return compilationRepository.findAllByPinnedOrPinnedIsNull(pinned, page)
                .stream().map(compilationMapper::compilationToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return compilationRepository.findById(compId)
                .map(compilationMapper::compilationToCompilationDto)
                .orElseThrow(() -> new NotFoundException(compId, Compilation.class));
    }
}
