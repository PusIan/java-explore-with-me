package ru.practicum.explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    private Collection<ParticipationRequestDto> confirmedRequests;
    private Collection<ParticipationRequestDto> rejectedRequests;
}
