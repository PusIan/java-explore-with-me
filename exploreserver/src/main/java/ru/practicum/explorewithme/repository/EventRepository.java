package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
