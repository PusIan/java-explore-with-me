package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.explorewithme.model.ViewStats(eh.app, eh.uri, " +
            "(case when :unique = true then count(distinct(eh.ip)) else count(eh.id) end))" +
            "from EndpointHit as eh " +
            "where (eh.timestamp >= :start or cast(:start as date) is null) " +
            "and (eh.timestamp <= :end or cast(:end as date) is null) " +
            "and (eh.uri in :uris or :uris is null ) " +
            "group by eh.app, eh.uri " +
            "order by (case when :unique = true then count(distinct(eh.ip)) else count(eh.id) end) desc")
    Collection<ViewStats> countStatByStartEndUriUnique(LocalDateTime start, LocalDateTime end, Collection<String> uris, Boolean unique);
}
