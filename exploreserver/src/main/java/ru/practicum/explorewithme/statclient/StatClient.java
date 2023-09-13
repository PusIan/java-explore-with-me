package ru.practicum.explorewithme.statclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.exception.BadRequestException;
import ru.practicum.explorewithme.model.Event;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.explorewithme.utils.Constants.DATE_TIME_FORMAT;
import static ru.practicum.explorewithme.utils.Constants.SYSTEM_NAME;

@Service
public class StatClient extends BaseClient {
    private static final String API_PREFIX = "";

    @Autowired
    public StatClient(@Value("${stat-gateway.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    private ResponseEntity<Object> createStatHitInternal(EndpointHitDto endpointHitDto) {
        return post("hit", endpointHitDto);
    }

    public void createStatHit(String ip, String uri) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setIp(ip);
        endpointHitDto.setUri(uri);
        endpointHitDto.setTimestamp(LocalDateTime.now());
        endpointHitDto.setApp(SYSTEM_NAME);
        createStatHitInternal(endpointHitDto);
    }

    public void createStatHitForEvents(String ip, Collection<Long> eventIds) {
        for (Long eventId : eventIds) {
            createStatHit(ip, "/events/" + eventId);
        }
    }

    private ResponseEntity<Object> getStatHitInternal(LocalDateTime start, LocalDateTime end, Collection<String> uris, Boolean unique) throws UnsupportedEncodingException {
        String encodedStart = encodeQueryDateToString(start);
        String encodedEnd = encodeQueryDateToString(end);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", encodedStart);
        parameters.put("end", encodedEnd);
        parameters.put("unique", unique);
        ResponseEntity<Object> result;
        if (uris != null) {
            parameters.put("uris", String.join(",", uris));
            result = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        } else {
            result = get("/stats?start={start}&end={end}&unique={unique}", parameters);
        }
        return result;
    }

    private String encodeQueryDateToString(LocalDateTime date) {
        return URLEncoder.encode(date.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)), StandardCharsets.UTF_8);
    }

    public Map<Long, Long> getViewsForEvents(Collection<Event> events) {
        HashMap<Long, Long> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            for (Event event : events) {
                String answer = objectMapper.writeValueAsString(getStatHitInternal(event.getCreatedOn(), LocalDateTime.now(),
                        List.of("/events/" + event.getId()), true).getBody());
                Collection<ViewStatsDto> response = new ObjectMapper().readValue(answer, new TypeReference<>() {
                });
                if (!response.isEmpty()) {
                    result.put(event.getId(), response.stream().findFirst().get().getHits());
                }
            }
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return result;
    }
}
