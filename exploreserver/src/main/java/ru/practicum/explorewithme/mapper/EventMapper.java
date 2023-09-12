package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.dto.EventFullDto;
import ru.practicum.explorewithme.dto.EventShortDto;
import ru.practicum.explorewithme.dto.NewEventDto;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {UserMapper.class, LocationMapper.class, CategoryMapper.class})
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    Event newEventDtoToEvent(NewEventDto newEventDto, Category category, User initiator);

    EventFullDto eventToEventFullDto(Event event);

    Collection<EventFullDto> eventsToEventsFullDto(Collection<Event> events);

    EventShortDto eventToEventShortDto(Event event);

    default Long eventToLong(Event event) {
        return event.getId();
    }
}
