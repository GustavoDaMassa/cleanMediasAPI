package br.com.gustavohenrique.cleanmediasapi.application.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateCourseRequest(
        @NotBlank String name,
        @NotBlank String averageMethod,
        @Positive double cutOffGrade
) {}
