package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CategoryMapper;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.mapper.LocationMapper;
import ru.practicum.explorewithme.mapper.RequestMapper;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.model.Request;
import ru.practicum.explorewithme.model.RequestStatus;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.RequestRepository;
import ru.practicum.explorewithme.statclient.StatClient;
import ru.practicum.explorewithme.utils.Utilities;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final CategoryMapper categoryMapper;
    private final LocationMapper locationMapper;
    private final StatClient statClient;

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Event savedEvent = eventRepository.save(eventMapper.newEventDtoToEvent(newEventDto, userId, new Event()));
        return eventMapper.eventToEventFullDto(savedEvent);
    }

    @Override
    public Collection<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        Pageable pageable = Utilities.getPageable(from, size, Sort.by("id").ascending());
        return eventRepository.findAllByInitiator_Id(userId, pageable)
                .stream().map(eventMapper::eventToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        return eventRepository.findByInitiator_IdAndId(userId, eventId)
                .map(eventMapper::eventToEventFullDto)
                .orElseThrow(() -> new NotFoundException(String.format("event %s not found for user %s", eventId, userId)));
    }

    @Override
    public EventFullDto patchEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(eventId, Event.class)
        );
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Event with status Published can not modified");
        }
        if (updateEventUserRequest.getStateAction() == UpdateEventUserRequestStateAction.CANCEL_REVIEW) {
            event.setState(EventState.CANCELED);
        }
        if (updateEventUserRequest.getStateAction() == UpdateEventUserRequestStateAction.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("event date is before than now more than two hours");
        }
        Optional.ofNullable(updateEventUserRequest.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(updateEventUserRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEventUserRequest.getCategory()).ifPresent(categoryMapper::toCategoryFromLong);
        Optional.ofNullable(updateEventUserRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(updateEventUserRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEventUserRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEventUserRequest.getLocationDto()).ifPresent(locationMapper::locationDtoToLocation);
        Optional.ofNullable(updateEventUserRequest.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(updateEventUserRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        return eventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventRequestStatusUpdateResult patchRequests(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId, Event.class));
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new ConflictException("No need to confirm Requests for Event with participantLimit == 0 or requestModeration == false");
        }
        Collection<Request> requests = requestRepository.findAllByEvent_IdAndEvent_Initiator_IdAndIdIn(eventId, userId, eventRequestStatusUpdateRequest.getRequestIds());
        if (!requests.stream().map(Request::getStatus).allMatch(x -> x.equals(RequestStatus.PENDING))) {
            throw new ConflictException("Requests only with status Pending can be confirmed");
        }
        Collection<Request> confirmedRequests = new ArrayList<>();
        Collection<Request> rejectedRequests = new ArrayList<>();
        int numberOfCurrentConfirmedRequests = requestRepository.findAllByEvent_IdAndStatus(eventId, RequestStatus.CONFIRMED).size();
        if (numberOfCurrentConfirmedRequests == event.getParticipantLimit()) {
            throw new ConflictException("no free slots in event");
        }
        for (Request request : requests) {
            if (numberOfCurrentConfirmedRequests < event.getParticipantLimit()
                    && eventRequestStatusUpdateRequest.getStatus() == EventRequestStatusUpdateRequestStatus.CONFIRMED) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(request);
                numberOfCurrentConfirmedRequests++;
            } else {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(request);
            }
        }
        requestRepository.saveAll(requests);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(requestMapper.requestsToParticipationRequestsDto(confirmedRequests));
        result.setRejectedRequests(requestMapper.requestsToParticipationRequestsDto(rejectedRequests));
        return result;
    }

    @Override
    public Collection<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        return eventRepository.findByInitiator_IdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException(String.format("event %s not found for user %s", eventId, userId)))
                .getRequests()
                .stream().map(requestMapper::requestToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<EventFullDto> searchEvents(Collection<Long> users, Collection<EventState> states, Collection<Long> categories,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable page = Utilities.getPageable(from, size, Sort.by("id").ascending());
        return eventRepository.searchEvents(users, states, categories, rangeStart, rangeEnd, page)
                .stream().map(eventMapper::eventToEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto patchEventByAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId, Event.class));
        if (updateEventAdminRequest.getStateAction() == UpdateEventAdminRequestStateAction.PUBLISH_EVENT) {
            if (event.getState() != EventState.PENDING) {
                throw new ConflictException("Only event in status Pending can be Published");
            }
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else if (updateEventAdminRequest.getStateAction() == UpdateEventAdminRequestStateAction.REJECT_EVENT) {
            if (event.getState() != EventState.PENDING) {
                throw new ConflictException("Only event with status Pending can be rejected");
            }
            event.setState(EventState.CANCELED);
            event.setPublishedOn(null);
        }
        if (event.getPublishedOn() != null &&
                event.getEventDate().isBefore(event.getPublishedOn().minusHours(1))) {
            throw new ConflictException("EventDate must got not far than 1 hour before publish date");
        }
        Optional.ofNullable(updateEventAdminRequest.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(updateEventAdminRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEventAdminRequest.getCategory()).ifPresent(categoryMapper::toCategoryFromLong);
        Optional.ofNullable(updateEventAdminRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(updateEventAdminRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEventAdminRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEventAdminRequest.getLocationDto()).ifPresent(locationMapper::locationDtoToLocation);
        Optional.ofNullable(updateEventAdminRequest.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(updateEventAdminRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        return eventMapper.eventToEventFullDto(eventRepository.save(event));
    }

    @Override
    public Collection<EventShortDto> searchEventsPublic(String text, Collection<Long> categories, Boolean paid,
                                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                        EventSearchOrderBy sort, Integer from, Integer size, HttpServletRequest request) {
        Sort processedSort = Sort.unsorted();
        switch (sort) {
            case EVENT_DATE:
                processedSort = Sort.by("eventDate").ascending();
                break;
            case VIEWS:
                processedSort = Sort.unsorted();
                break;
        }
        Pageable pageable = Utilities.getPageable(from, size, processedSort);
        rangeStart = Optional.ofNullable(rangeStart).orElse(LocalDateTime.now());
        Collection<Event> events = eventRepository.searchEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable)
                .toList();
        Collection<EventShortDto> eventShortDtos = events
                .stream().map(eventMapper::eventToEventShortDto)
                .collect(Collectors.toList());
        statClient.createStatHit(request.getRemoteAddr(), request.getRequestURI());
        statClient.createStatHitForEvents(request.getRemoteAddr(), events.stream().map(Event::getId).collect(Collectors.toList()));
        Map<Long, Long> views = statClient.getViewsForEventIds(events);
        fillEventsShortDto(eventShortDtos, views);
        if (sort.equals(EventSearchOrderBy.VIEWS)) {
            eventShortDtos = eventShortDtos
                    .stream().sorted(Comparator.comparing(x -> -x.getViews()))
                    .collect(Collectors.toList())
                    .subList(from, from + size + 1);
        }
        return eventShortDtos;
    }

    @Override
    public EventFullDto getEventByIdPublic(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .filter(x -> x.getState().equals(EventState.PUBLISHED))
                .orElseThrow(() -> new NotFoundException(id, Event.class));
        EventFullDto eventFullDto = eventMapper.eventToEventFullDto(event);
        statClient.createStatHit(request.getRemoteAddr(), request.getRequestURI());
        Map<Long, Long> views = statClient.getViewsForEventIds(List.of(event));
        fillEventsFullDto(List.of(eventFullDto), views);
        return eventFullDto;
    }

    private void fillEventsFullDto(Collection<EventFullDto> eventFullDtos, Map<Long, Long> views) {
        for (EventFullDto eventFullDto : eventFullDtos) {
            eventFullDto.setViews(views.getOrDefault(eventFullDto.getId(), 0L));
        }
    }

    private void fillEventsShortDto(Collection<EventShortDto> eventShortDtos, Map<Long, Long> views) {
        for (EventShortDto eventShortDto : eventShortDtos) {
            eventShortDto.setViews(views.getOrDefault(eventShortDto.getId(), 0L));
        }
    }
}
