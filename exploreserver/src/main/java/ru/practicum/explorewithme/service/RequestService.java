package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.ParticipationRequestDto;

import java.util.Collection;

public interface RequestService {
    ParticipationRequestDto createRequest(Long userId, Long eventId);

    Collection<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
