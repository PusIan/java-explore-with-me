package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.EventFullDto;
import ru.practicum.explorewithme.dto.UpdateEventAdminRequest;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.service.EventService;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.practicum.explorewithme.utils.Constants.PAGE_FROM_DEFAULT;
import static ru.practicum.explorewithme.utils.Constants.PAGE_SIZE_DEFAULT;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/events")
@Validated
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public Collection<EventFullDto> searchEvents(@RequestParam(required = false) Collection<Long> users,
                                                 @RequestParam(required = false) Collection<EventState> states,
                                                 @RequestParam(required = false) Collection<Long> categories,
                                                 @RequestParam(required = false) LocalDateTime rangeStart,
                                                 @RequestParam(required = false) LocalDateTime rangeEnd,
                                                 @RequestParam(defaultValue = PAGE_FROM_DEFAULT) Integer from,
                                                 @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) Integer size) {
        return eventService.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "{eventId}")
    public EventFullDto updateEvent(@RequestBody @Validated UpdateEventAdminRequest updateEventAdminRequest,
                                    @PathVariable Long eventId) {
        return eventService.patchEventByAdmin(updateEventAdminRequest, eventId);
    }
}
