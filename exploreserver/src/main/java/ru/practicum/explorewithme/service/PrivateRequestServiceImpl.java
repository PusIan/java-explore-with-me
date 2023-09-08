package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.mapper.RequestMapper;
import ru.practicum.explorewithme.repository.RequestRepository;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        return requestMapper.requestToParticipationRequestDto(
                requestRepository.save(requestMapper.parametersToRequest(userId, eventId)));
    }
}
