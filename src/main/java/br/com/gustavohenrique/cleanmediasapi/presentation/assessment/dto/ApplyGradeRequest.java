package br.com.gustavohenrique.cleanmediasapi.presentation.assessment.dto;

import jakarta.validation.constraints.NotNull;

public record ApplyGradeRequest(
        @NotNull Double grade
) {}
