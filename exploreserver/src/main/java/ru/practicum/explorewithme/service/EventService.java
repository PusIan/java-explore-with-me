package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.model.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    Collection<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto updateEventById(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventRequestStatusUpdateResult updateRequests(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, Long userId, Long eventId);

    Collection<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    Collection<EventFullDto> searchEvents(Collection<Long> users, Collection<EventState> states, Collection<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId);

    Collection<EventShortDto> searchEventsPublic(String text, Collection<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, EventSearchOrderBy sort, Integer from, Integer size, HttpServletRequest request, Integer ratingFrom, Integer ratingTo);

    EventFullDto getEventByIdPublic(Long id, HttpServletRequest request);
}
