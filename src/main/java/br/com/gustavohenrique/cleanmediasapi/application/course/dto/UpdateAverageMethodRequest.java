package br.com.gustavohenrique.cleanmediasapi.application.course.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAverageMethodRequest(@NotBlank String averageMethod) {}
