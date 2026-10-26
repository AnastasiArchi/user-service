package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")

@Tag(
        name = "Пользователи",
        description = "API для создания, получения, обновления и удаления пользователей"
)

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает данные пользователя и ссылки HATEOAS"
    )

    @GetMapping("/{id}")
    public EntityModel<UserResponseDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        UserResponseDto response = new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge());

        return EntityModel.of(response, linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(), linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"));
    }

    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список пользователей со ссылками на отдельные ресурсы"
    )

    @GetMapping
    public CollectionModel<EntityModel<UserResponseDto>> getAllUsers() {

        List<EntityModel<UserResponseDto>> users = userService.getAllUsers().stream().map(user -> {
            UserResponseDto response = new UserResponseDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getAge());

            return EntityModel.of(response, linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
        }).toList();

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserResponseDto> createUser(@RequestBody UserRequestDto request) {
        User user = userService.createUser(
                request.name(),
                request.email(),
                request.age());

        UserResponseDto response = new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge());

        return EntityModel.of(response, linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(), linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"));
    }

    @Operation(
            summary = "Обновить пользователя",
            description = "Обновляет данные существующего пользователя и возвращает ссылки HATEOAS"
    )

    @PutMapping("/{id}")
    public EntityModel<UserResponseDto> updateUser(
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

        UserResponseDto response = new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge()
        );

        return EntityModel.of(
                response,
                linkTo(methodOn(UserController.class)
                        .getUserById(id))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .getAllUsers())
                        .withRel("users")
        );
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по идентификатору"
    )

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}