package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.explorewithme.dto.EventFullDto;
import ru.practicum.explorewithme.dto.EventShortDto;
import ru.practicum.explorewithme.dto.NewEventDto;
import ru.practicum.explorewithme.model.Event;

import java.util.Collection;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, UserMapper.class, LocationMapper.class, CategoryMapper.class})
public interface EventMapper {

    @Mapping(target = "initiator", source = "userId")
    Event newEventDtoToEvent(NewEventDto newEventDto, Long userId, @MappingTarget Event event);

    EventFullDto eventToEventFullDto(Event event);

    Collection<EventFullDto> eventsToEventsFullDto(Collection<Event> events);

    EventShortDto eventToEventShortDto(Event event);

    Event longToEvent(Long eventId);

    Set<Event> idsToEvents(Collection<Long> ids);

    default Long eventToLong(Event event) {
        return event.getId();
    }
}
