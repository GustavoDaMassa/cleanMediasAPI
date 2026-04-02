package br.com.gustavohenrique.cleanmediasapi.application.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password
) {}
