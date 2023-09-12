package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.dto.CompilationDto;
import ru.practicum.explorewithme.dto.NewCompilationDto;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;

import java.util.Set;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {
    @Mapping(target = "events", source = "events")
    Compilation newCompilationDtoToCompilation(NewCompilationDto newCompilationDto, Set<Event> events);

    CompilationDto compilationToCompilationDto(Compilation compilation);
}
