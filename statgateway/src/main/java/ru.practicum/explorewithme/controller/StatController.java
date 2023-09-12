package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.client.StatClient;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.exception.BadRequestException;

import java.io.UnsupportedEncodingException;
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
    private final StatClient statClient;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createStatHit(@RequestBody @Validated EndpointHitDto endpointHitDto) {
        return statClient.createStatHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStatHit(@RequestParam String start,
                                             @RequestParam String end,
                                             @RequestParam(required = false) Collection<String> uris,
                                             @RequestParam(defaultValue = "false") Boolean unique) throws UnsupportedEncodingException {
        LocalDateTime decodedStart = getDateFromEncodedString(start);
        LocalDateTime decodedEnd = getDateFromEncodedString(end);
        return statClient.getStatHit(decodedStart, decodedEnd, uris, unique);
    }

    private LocalDateTime getDateFromEncodedString(String date) {
        return date != null ? LocalDateTime.parse(URLDecoder.decode(date, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)) : null;
    }
}