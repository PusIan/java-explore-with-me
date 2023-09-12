package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.UserDto;
import ru.practicum.explorewithme.service.UserService;

import java.util.Collection;

import static ru.practicum.explorewithme.utils.Constants.PAGE_FROM_DEFAULT;
import static ru.practicum.explorewithme.utils.Constants.PAGE_SIZE_DEFAULT;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Validated UserDto userDto) {
        return userService.createUser(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping
    public Collection<UserDto> getUsers(@RequestParam(required = false) Collection<Long> ids,
                                        @RequestParam(defaultValue = PAGE_FROM_DEFAULT) Integer from,
                                        @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) Integer size) {
        return userService.getUsers(ids, from, size);
    }
}
