package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.explorewithme.model.ViewStats(eh.app, eh.uri, count(1))" +
            "from EndpointHit as eh " +
            "where eh.timestamp >= :start and eh.timestamp < :end " +
            "and (eh.uri in :uris or :uris is null ) " +
            "group by eh.app, eh.uri " +
            "order by count(1) desc")
    Collection<ViewStats> countStatByStartEndUris(LocalDateTime start, LocalDateTime end, Collection<String> uris);

    @Query("select new ru.practicum.explorewithme.model.ViewStats(eh.app, eh.uri, count(distinct(eh.ip)))" +
            "from EndpointHit as eh " +
            "where eh.timestamp >= :start and eh.timestamp < :end " +
            "and (eh.uri in :uris or :uris is null ) " +
            "group by eh.app, eh.uri " +
            "order by count(distinct(eh.ip)) desc")
    Collection<ViewStats> countStatByStartEndUrisUniqueIps(LocalDateTime start, LocalDateTime end, Collection<String> uris);
}
