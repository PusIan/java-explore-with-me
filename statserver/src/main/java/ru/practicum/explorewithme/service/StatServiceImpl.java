package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.mapper.StatMapper;
import ru.practicum.explorewithme.model.ViewStats;
import ru.practicum.explorewithme.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;
    private final StatMapper statMapper;

    @Override
    @Transactional
    public EndpointHitDto createStatHit(EndpointHitDto endpointHitDto) {
        return statMapper.endpointHitToEndPointHitDto(
                statRepository.save(statMapper.endpointHitDtoToEndPointHit(endpointHitDto))
        );
    }

    @Override
    public Collection<ViewStatsDto> getStatHit(LocalDateTime start, LocalDateTime end, Collection<String> uris, boolean unique) {
        Collection<ViewStats> viewStats = new ArrayList<>();
        if (uris != null) {
            for (String uri : uris) {
                viewStats.addAll(statRepository.countStatByStartEndUriUnique(start, end, uri, unique));
            }
        } else {
            viewStats.addAll(statRepository.countStatByStartEndUriUnique(start, end, null, unique));
        }
        return statMapper.viewStatsToViewStatsDtoCollection(viewStats)
                .stream().sorted(Comparator.comparing(x -> -x.getHits()))
                .collect(Collectors.toList());
    }
}
