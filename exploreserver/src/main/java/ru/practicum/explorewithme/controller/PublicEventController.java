package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.EventFullDto;
import ru.practicum.explorewithme.dto.EventSearchOrderBy;
import ru.practicum.explorewithme.dto.EventShortDto;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.service.EventService;
import ru.practicum.explorewithme.statclient.StatClient;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.Collection;

import static ru.practicum.explorewithme.utils.Constants.PAGE_FROM_DEFAULT;
import static ru.practicum.explorewithme.utils.Constants.PAGE_SIZE_DEFAULT;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/events")
@Validated
public class PublicEventController {
    private final EventService eventService;
    private final StatClient statClient;

    @GetMapping
    Collection<EventShortDto> searchEventsPublic(@RequestParam(required = false) String text,
                                                 @RequestParam(required = false) Collection<Long> categories,
                                                 @RequestParam(required = false) Boolean paid,
                                                 @RequestParam(required = false) LocalDateTime rangeStart,
                                                 @RequestParam(required = false) @Validated @Future LocalDateTime rangeEnd,
                                                 @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                 @RequestParam(defaultValue = "EVENT_DATE") EventSearchOrderBy sort,
                                                 @RequestParam(defaultValue = PAGE_FROM_DEFAULT) Integer from,
                                                 @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) Integer size,
                                                 HttpServletRequest request) {
        if (rangeEnd != null && rangeStart != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("rangeEnd should be after rangeStart");
        }
        Collection<EventShortDto> eventShortDtos = eventService.searchEventsPublic(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
        return eventShortDtos;
    }

    @GetMapping("{id}")
    EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        EventFullDto eventFullDto = eventService.getEventByIdPublic(id, request);
        return eventFullDto;
    }
}

