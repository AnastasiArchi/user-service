package org.example.controller;

import org.example.dto.UserResponseDto;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.example.dto.UserRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge()
        );
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getAge()
                ))
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody UserRequestDto request) {
        User user = userService.createUser(
                request.name(),
                request.email(),
                request.age()
        );

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge()
        );
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDto request
    ) {
        userService.updateUser(
                id,
                request.name(),
                request.email(),
                request.age()
        );

        User user = userService.getUserById(id);

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}