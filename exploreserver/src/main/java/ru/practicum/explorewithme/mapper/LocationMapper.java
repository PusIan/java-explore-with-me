package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.dto.LocationDto;
import ru.practicum.explorewithme.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location locationDtoToLocation(LocationDto locationDto);

    LocationDto locationToLocationDto(Location location);
}
