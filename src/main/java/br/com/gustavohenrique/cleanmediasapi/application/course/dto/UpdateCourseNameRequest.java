package br.com.gustavohenrique.cleanmediasapi.application.course.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCourseNameRequest(@NotBlank String name) {}
