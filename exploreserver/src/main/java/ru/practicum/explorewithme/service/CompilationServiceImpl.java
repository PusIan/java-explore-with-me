package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.CompilationDto;
import ru.practicum.explorewithme.dto.NewCompilationDto;
import ru.practicum.explorewithme.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CompilationMapper;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.utils.Utilities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();
        Optional.ofNullable(newCompilationDto.getEvents()).ifPresent(eventIds ->
                events.addAll(new HashSet<>(eventRepository.findAllById(eventIds))));
        return compilationMapper.compilationToCompilationDto(
                compilationRepository.save(compilationMapper.newCompilationDtoToCompilation(newCompilationDto, events)));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(compId, Compilation.class)
        );
        Optional.ofNullable(updateCompilationRequest.getEvents()).ifPresent(eventIds -> compilation.setEvents(
                new HashSet<>(eventRepository.findAllById(eventIds))
        ));
        Optional.ofNullable(updateCompilationRequest.getPinned()).ifPresent(compilation::setPinned);
        Optional.ofNullable(updateCompilationRequest.getTitle()).ifPresent(compilation::setTitle);
        return compilationMapper.compilationToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable page = Utilities.getPageable(from, size, Sort.by("id").ascending());
        return compilationRepository.findAllByPinned(pinned, page)
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
