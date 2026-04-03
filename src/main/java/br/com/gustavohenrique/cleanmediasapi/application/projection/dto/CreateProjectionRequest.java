package br.com.gustavohenrique.cleanmediasapi.application.projection.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectionRequest(@NotBlank String name) {}
