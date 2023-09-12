package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Request;
import ru.practicum.explorewithme.model.RequestStatus;

import java.util.Collection;
import java.util.Optional;


public interface RequestRepository extends JpaRepository<Request, Long> {
    Collection<Request> findAllByRequester_Id(Long requesterId);

    Optional<Request> findByIdAndRequester_Id(Long id, Long requesterId);

    Collection<Request> findAllByEvent_IdAndEvent_Initiator_IdAndIdIn(Long eventId, Long eventInitiatorId, Collection<Long> id);

    Collection<Request> findAllByEvent_IdAndStatus(Long eventId, RequestStatus status);
}
