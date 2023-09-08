package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.model.Request;

@Mapper(componentModel = "spring", uses = {UserMapper.class, EventMapper.class})
public interface RequestMapper {

    @Mapping(target = "event", source = "eventId", qualifiedByName = "LongToEvent")
    @Mapping(target = "requester", source = "userId")
    Request parametersToRequest(Long userId, Long eventId);

    ParticipationRequestDto requestToParticipationRequestDto(Request request);
}
