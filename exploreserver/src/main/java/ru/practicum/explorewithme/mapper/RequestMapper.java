package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.model.Request;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {UserMapper.class, EventMapper.class})
public interface RequestMapper {

    @Mapping(target = "event", source = "eventId")
    @Mapping(target = "requester", source = "userId")
    Request parametersToRequest(Long userId, Long eventId);

    ParticipationRequestDto requestToParticipationRequestDto(Request request);

    Collection<ParticipationRequestDto> requestsToParticipationRequestsDto(Collection<Request> requests);
}
