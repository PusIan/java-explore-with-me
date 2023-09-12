package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.EventUserLikes;

public interface LikeService {
    void addLikeToEvent(Long userId, Long eventId, Boolean like);

    void deleteLikeFromEvent(Long userId, Long eventId, Boolean like);

    EventUserLikes getLikesInfoForEvent(Long id, Integer from, Integer size);
}
