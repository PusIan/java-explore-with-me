package ru.practicum.explorewithme.dto;

import lombok.*;

import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventUserLikes {
    Collection<UserDto> userLikes;
    Integer userLikesCnt;
    Collection<UserDto> userDislikes;
    Integer userDislikesCnt;
}
