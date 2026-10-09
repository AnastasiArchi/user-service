package org.example.dto;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        Integer age
) {
}