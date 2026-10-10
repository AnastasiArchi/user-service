package org.example.controller;

import org.example.entity.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldReturnUserById() throws Exception {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");
        when(user.getEmail()).thenReturn("maria@test.com");
        when(user.getAge()).thenReturn(30);

        when(userService.getUserById(1L))
                .thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maria"))
                .andExpect(jsonPath("$.email").value("maria@test.com"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");
        when(user.getEmail()).thenReturn("maria@test.com");
        when(user.getAge()).thenReturn(30);

        when(userService.getAllUsers())
                .thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Maria"))
                .andExpect(jsonPath("$[0].email").value("maria@test.com"))
                .andExpect(jsonPath("$[0].age").value(30));
    }

    @Test
    void shouldCreateUser() throws Exception {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria");
        when(user.getEmail()).thenReturn("maria@test.com");
        when(user.getAge()).thenReturn(30);

        when(userService.createUser(
                "Maria",
                "maria@test.com",
                30
        )).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Maria",
                                  "email": "maria@test.com",
                                  "age": 30
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maria"))
                .andExpect(jsonPath("$.email").value("maria@test.com"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void shouldUpdateUser() throws Exception {

        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Maria Updated");
        when(user.getEmail()).thenReturn("maria.updated@test.com");
        when(user.getAge()).thenReturn(31);

        when(userService.getUserById(1L))
                .thenReturn(user);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Maria Updated",
                                  "email": "maria.updated@test.com",
                                  "age": 31
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maria Updated"))
                .andExpect(jsonPath("$.email").value("maria.updated@test.com"))
                .andExpect(jsonPath("$.age").value(31));
    }

    @Test
    void shouldDeleteUser() throws Exception {

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }
}