package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.EventFullDto;
import ru.practicum.explorewithme.dto.EventShortDto;
import ru.practicum.explorewithme.dto.NewEventDto;
import ru.practicum.explorewithme.dto.UpdateEventUserRequest;

import java.util.Collection;

public interface PrivateEventService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    Collection<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto patchEventById(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);
}
