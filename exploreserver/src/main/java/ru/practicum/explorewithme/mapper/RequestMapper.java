package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.model.Request;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {UserMapper.class, EventMapper.class})
public interface RequestMapper {
    ParticipationRequestDto requestToParticipationRequestDto(Request request);

    Collection<ParticipationRequestDto> requestsToParticipationRequestsDto(Collection<Request> requests);
}
