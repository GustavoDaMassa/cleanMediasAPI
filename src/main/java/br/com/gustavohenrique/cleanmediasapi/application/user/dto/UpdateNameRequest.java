package br.com.gustavohenrique.cleanmediasapi.application.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateNameRequest(@NotBlank String name) {}
