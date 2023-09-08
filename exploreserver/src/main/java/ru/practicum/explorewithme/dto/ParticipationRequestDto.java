package ru.practicum.explorewithme.dto;

import java.time.LocalDateTime;

public class ParticipationRequestDto {
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private RequestStatusDto status;
}
