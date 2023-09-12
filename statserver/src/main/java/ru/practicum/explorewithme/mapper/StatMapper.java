package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface StatMapper {
    EndpointHitDto endpointHitToEndPointHitDto(EndpointHit endpointHit);

    EndpointHit endpointHitDtoToEndPointHit(EndpointHitDto endpointHitDto);

    ViewStatsDto viewStatsToViewStatsDto(ViewStats viewStats);

    ViewStats viewStatsDtoToViewStats(ViewStatsDto viewStatsDto);

    Collection<ViewStatsDto> viewStatsToViewStatsDtoCollection(Collection<ViewStats> viewStats);
}
