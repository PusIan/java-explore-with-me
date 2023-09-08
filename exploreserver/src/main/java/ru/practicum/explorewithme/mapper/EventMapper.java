package ru.practicum.explorewithme.mapper;

import org.mapstruct.*;
import ru.practicum.explorewithme.dto.EventFullDto;
import ru.practicum.explorewithme.dto.NewEventDto;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class, UserMapper.class, LocationMapper.class, CategoryMapper.class})
public interface EventMapper {

    @Mapping(target = "initiator", source = "userId")
    Event newEventDtoToEvent(NewEventDto newEventDto, Long userId, @MappingTarget Event event);

    EventFullDto eventToEventFullDto(Event event);

    @Named("LongToEvent")
    Event LongToEvent(Long eventId);
}
