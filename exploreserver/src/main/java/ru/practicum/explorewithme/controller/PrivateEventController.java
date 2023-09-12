package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.service.EventService;

import java.util.Collection;

import static ru.practicum.explorewithme.utils.Constants.PAGE_FROM_DEFAULT;
import static ru.practicum.explorewithme.utils.Constants.PAGE_SIZE_DEFAULT;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public Collection<EventShortDto> getEventsByUserId(@PathVariable Long userId,
                                                       @RequestParam(defaultValue = PAGE_FROM_DEFAULT) Integer from,
                                                       @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) Integer size) {
        return eventService.getEventsByUserId(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@RequestBody @Validated NewEventDto newEventDto, @PathVariable Long userId) {
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("{eventId}")
    public EventFullDto getEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("{eventId}")
    public EventFullDto updateEventById(@RequestBody @Validated UpdateEventUserRequest updateEventUserRequest,
                                        @PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.patchEventByUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("{eventId}/requests")
    public Collection<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                           @PathVariable Long eventId) {
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@RequestBody @Validated EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                         @PathVariable Long userId,
                                                         @PathVariable Long eventId) {
        return eventService.patchRequests(eventRequestStatusUpdateRequest, userId, eventId);
    }
}
