package ru.practicum.explorewithme.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.dto.EndpointHitDto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.practicum.explorewithme.utils.Constants.DATE_TIME_FORMAT;

@Service
public class StatClient extends BaseClient {
    private static final String API_PREFIX = "";

    @Autowired
    public StatClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createStatHit(EndpointHitDto endpointHitDto) {
        return post("hit", endpointHitDto);
    }

    public ResponseEntity<Object> getStatHit(LocalDateTime start, LocalDateTime end, Collection<String> uris, Boolean unique) throws UnsupportedEncodingException {
        String encodedStart = encodeQueryDateToString(start);
        String encodedEnd = encodeQueryDateToString(end);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", encodedStart);
        parameters.put("end", encodedEnd);
        parameters.put("unique", unique);
        if (uris != null) {
            parameters.put("uris", String.join(",", uris));
            return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        }
        return get("/stats?start={start}&end={end}&unique={unique}", parameters);
    }

    private String encodeQueryDateToString(LocalDateTime date) {
        return URLEncoder.encode(date.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)), StandardCharsets.UTF_8);
    }
}
