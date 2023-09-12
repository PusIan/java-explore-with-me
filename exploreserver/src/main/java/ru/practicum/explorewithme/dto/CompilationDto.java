package ru.practicum.explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Collection<EventShortDto> events;
    private Long id;
    private Boolean pinned = false;
    private String title;
}

