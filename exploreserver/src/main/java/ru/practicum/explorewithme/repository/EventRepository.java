package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventState;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiator_Id(Long initiatorId, Pageable pageable);

    Optional<Event> findByInitiator_IdAndId(Long initiatorId, Long id);

    @Query("select e from  Event e " +
            "Where (e.initiator.id in :users or :users is null) " +
            "and (e.state in :states or :states is null) " +
            "and (e.category.id in :categories or :categories is null) " +
            "and (e.eventDate >= :rangeStart or cast(:rangeStart as date) is null) " +
            "and (e.eventDate < :rangeEnd or cast(:rangeEnd as date) is null)")
    Page<Event> searchEvents(Collection<Long> users, Collection<EventState> states, Collection<Long> categories,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e " +
            "Where e.state = ru.practicum.explorewithme.model.EventState.PUBLISHED " +
            "and (lower(e.annotation) like lower('%'||:text||'%') " +
            "    or lower(e.description) like lower('%'||:text||'%') or :text is null) " +
            "and (e.category.id in :categories or :categories is null) " +
            "and (e.paid = :paid or :paid is null) " +
            "and (e.eventDate >= :rangeStart or cast(:rangeStart as date) is null) " +
            "and (e.eventDate < :rangeEnd or cast(:rangeEnd as date) is null) " +
            "and (:onlyAvailable = false " +
            "    or e.confirmedRequests < e.participantLimit)")
    Page<Event> searchEventsPublic(String text, Collection<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Event> findEventById(Long id);
}
