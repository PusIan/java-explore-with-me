package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.UserDto;
import ru.practicum.explorewithme.mapper.UserMapper;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.utils.Utilities;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        return userMapper.userToUserDto(userRepository.save(userMapper.userDtoToUser(userDto)));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Collection<UserDto> getUsers(Collection<Long> ids, Integer from, Integer size) {
        Pageable pageable = Utilities.getPageable(from, size, Sort.by("id").ascending());
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(pageable).stream().map(userMapper::userToUserDto).collect(Collectors.toList());
        }
        return userRepository.findAllByIdIn(ids, pageable).stream().map(userMapper::userToUserDto).collect(Collectors.toList());
    }
}
