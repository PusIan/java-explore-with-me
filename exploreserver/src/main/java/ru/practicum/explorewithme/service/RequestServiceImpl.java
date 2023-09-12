package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.RequestMapper;
import ru.practicum.explorewithme.model.*;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.RequestRepository;
import ru.practicum.explorewithme.repository.UserRepository;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId, User.class));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId, Event.class));
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("request can be created only for published event");
        }
        if (Objects.equals(userId, event.getInitiator().getId())) {
            throw new ConflictException("event initiator can not create request for participation");
        }
        if (event.getParticipantLimit() != 0
                && Objects.equals(event.getConfirmedRequests(), event.getParticipantLimit())) {
            throw new ConflictException("no free slots in event");
        }
        Request request = new Request();
        if (event.getParticipantLimit() == 0 ||
                !event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        request.setRequester(user);
        request.setEvent(event);

        return requestMapper.requestToParticipationRequestDto(
                requestRepository.save(request));
    }

    @Override
    public Collection<ParticipationRequestDto> getRequests(Long userId) {
        return requestRepository.findAllByRequester_Id(userId)
                .stream().map(requestMapper::requestToParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequester_Id(requestId, userId).orElseThrow(
                () -> new NotFoundException(String.format("Request %s not found for user %s", requestId, userId))
        );
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.requestToParticipationRequestDto(requestRepository.save(request));
    }
}
