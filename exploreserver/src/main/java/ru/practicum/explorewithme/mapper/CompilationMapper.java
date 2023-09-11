package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.dto.CompilationDto;
import ru.practicum.explorewithme.dto.NewCompilationDto;
import ru.practicum.explorewithme.model.Compilation;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {
    Compilation newCompilationDtoToCompilation(NewCompilationDto newCompilationDto);

    CompilationDto compilationToCompilationDto(Compilation compilation);
}
