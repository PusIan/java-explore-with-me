package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.dto.UserDto;
import ru.practicum.explorewithme.model.User;

@Mapper(componentModel = "spring", uses = ReferenceMapper.class)
public interface UserMapper {
    User UserDtoToUser(UserDto userDto);

    UserDto UserToUserDto(User user);

    User longToUser(Long id);
}
