package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Like;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findLikeByEvent_idAndUser_id(Long eventId, Long userId);

    Optional<Like> findLikeByEvent_IdAndUser_IdAndIsPositive(Long eventId, Long userId, Boolean isPositive);

    Page<Like> findLikeByEvent_IdAndIsPositive(Long eventId, Boolean isPositive, Pageable pageable);

    Integer countLikesByEvent_IdAndIsPositive(Long eventId, Boolean isPositive);
}
