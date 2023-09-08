package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.EventFullDto;
import ru.practicum.explorewithme.dto.EventShortDto;
import ru.practicum.explorewithme.dto.NewEventDto;
import ru.practicum.explorewithme.dto.UpdateEventUserRequest;
import ru.practicum.explorewithme.service.PrivateEventService;

import java.util.Collection;

import static ru.practicum.explorewithme.utils.Constants.PAGE_FROM_DEFAULT;
import static ru.practicum.explorewithme.utils.Constants.PAGE_SIZE_DEFAULT;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class PrivateEventController {
    private final PrivateEventService privateEventService;

    @GetMapping
    public Collection<EventShortDto> getEventsByUserId(@PathVariable Long userId,
                                                       @RequestParam(defaultValue = PAGE_FROM_DEFAULT) Integer from,
                                                       @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) Integer size) {
        return privateEventService.getEventsByUserId(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@RequestBody NewEventDto newEventDto, @PathVariable Long userId) {
        return privateEventService.createEvent(userId, newEventDto);
    }

    @GetMapping("{eventId}")
    public EventFullDto getEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        return privateEventService.getEventById(userId, eventId);
    }

    @PatchMapping("{eventId}")
    public EventFullDto patchEventById(@RequestBody UpdateEventUserRequest updateEventUserRequest,
                                       @PathVariable Long userId, @PathVariable Long eventId) {
        return privateEventService.patchEventById(userId, eventId, updateEventUserRequest);
    }
}
