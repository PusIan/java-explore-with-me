package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserDto userDto);

    void deleteUser(Long userId);

    Collection<UserDto> getUsers(Collection<Long> ids, Integer from, Integer size);
}
