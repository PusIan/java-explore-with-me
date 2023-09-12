package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.service.LikeService;


@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/users/{userId}/events/{eventId}/likes")
@Validated
public class PrivateLikeController {
    private final LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addLikeToEvent(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @RequestParam(defaultValue = "true") Boolean like) {
        likeService.addLikeToEvent(userId, eventId, like);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLikeFromEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestParam(required = false) Boolean like) {
        likeService.deleteLikeFromEvent(userId, eventId, like);
    }
}
