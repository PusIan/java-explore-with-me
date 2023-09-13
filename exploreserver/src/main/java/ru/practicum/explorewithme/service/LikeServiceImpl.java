package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.EventUserLikes;
import ru.practicum.explorewithme.dto.UserDto;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.UserMapper;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventState;
import ru.practicum.explorewithme.model.Like;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.LikeRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.utils.Utilities;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void addLikeToEvent(Long userId, Long eventId, Boolean like) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId, Event.class));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId, User.class));
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Event is not PUBLISHED, you can put like/dislike " +
                    "only on events in status PUBLISHED");
        }
        likeRepository.findLikeByEvent_idAndUser_id(eventId, userId).ifPresent(x -> {
            throw new ConflictException(getLikeString(x.getIsPositive()) + " already present, remove first");
        });
        Like likeForSaving = Like.builder()
                .event(event)
                .user(user)
                .isPositive(like)
                .build();
        likeRepository.save(likeForSaving);
    }

    @Override
    @Transactional
    public void deleteLikeFromEvent(Long userId, Long eventId, Boolean like) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId, Event.class));
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId, User.class));
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Event is not PUBLISHED, you can remove like/dislike " +
                    "only on events in status PUBLISHED");
        }
        Like likeForDeletion = likeRepository.findLikeByEvent_IdAndUser_IdAndIsPositive(eventId, userId, like)
                .orElseThrow(() -> new ConflictException("no " + getLikeString(like) + " is present"));
        likeRepository.delete(likeForDeletion);
    }

    @Override
    public EventUserLikes getLikesInfoForEvent(Long id, Integer from, Integer size) {
        eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, Event.class));
        Pageable pageable = Utilities.getPageable(from, size, Sort.by("id").ascending());
        Collection<UserDto> userLikes = likeRepository.findLikeByEvent_IdAndIsPositive(id, true, pageable)
                .stream().map(Like::getUser)
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
        Integer userLikesCnt = likeRepository.countLikesByEvent_IdAndIsPositive(id, true);
        Collection<UserDto> userDislikes = likeRepository.findLikeByEvent_IdAndIsPositive(id, false, pageable)
                .stream().map(Like::getUser)
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
        Integer userDislikesCnt = likeRepository.countLikesByEvent_IdAndIsPositive(id, false);
        return EventUserLikes.builder()
                .userLikesCnt(userLikesCnt)
                .userLikes(userLikes)
                .userDislikesCnt(userDislikesCnt)
                .userDislikes(userDislikes)
                .build();
    }

    private String getLikeString(Boolean like) {
        return like ? "like" : "dislike";
    }
}
