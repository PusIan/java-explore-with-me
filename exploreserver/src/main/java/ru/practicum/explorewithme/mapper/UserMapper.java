package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.dto.UserDto;
import ru.practicum.explorewithme.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);

    default Long userToLong(User user) {
        return user.getId();
    }
}
