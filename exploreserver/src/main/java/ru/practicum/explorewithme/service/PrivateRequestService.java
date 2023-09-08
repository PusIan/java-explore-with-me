package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.ParticipationRequestDto;

public interface PrivateRequestService {
    ParticipationRequestDto createRequest(Long userId, Long eventId);
}
