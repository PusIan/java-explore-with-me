package ru.practicum.explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {
    @Length(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Length(min = 20, max = 7000)
    private String description;
    @Future
    private LocalDateTime eventDate;
    private LocationDto locationDto;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private UpdateEventUserRequestStateAction stateAction;
    @Length(min = 3, max = 120)
    private String title;
}
