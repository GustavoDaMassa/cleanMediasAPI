package br.com.gustavohenrique.cleanmediasapi.application.projection.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProjectionNameRequest(@NotBlank String name) {}
