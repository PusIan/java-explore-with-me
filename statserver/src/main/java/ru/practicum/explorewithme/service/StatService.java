package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StatService {
    EndpointHitDto createStatHit(EndpointHitDto endpointHitDto);

    Collection<ViewStatsDto> getStatHit(LocalDateTime start, LocalDateTime end,
                                        Collection<String> uris, boolean unique);
}
