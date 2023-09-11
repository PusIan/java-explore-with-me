package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.service.StatService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static ru.practicum.explorewithme.utils.Constants.DATE_TIME_FORMAT;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "")
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createStatHit(@RequestBody EndpointHitDto endpointHitDto) {
        return statService.createStatHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public Collection<ViewStatsDto> getStatHit(@RequestParam String start,
                                               @RequestParam String end,
                                               @RequestParam(required = false) Collection<String> uris,
                                               @RequestParam(defaultValue = "false") Boolean unique) {
        LocalDateTime startDate = getDateFromEncodedString(start);
        LocalDateTime endDate = getDateFromEncodedString(end);
        return statService.getStatHit(startDate, endDate, uris, unique);
    }

    private LocalDateTime getDateFromEncodedString(String date) {
        return LocalDateTime.parse(URLDecoder.decode(date, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }
}