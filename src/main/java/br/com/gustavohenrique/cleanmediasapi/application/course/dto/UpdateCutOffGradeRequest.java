package br.com.gustavohenrique.cleanmediasapi.application.course.dto;

import jakarta.validation.constraints.Positive;

public record UpdateCutOffGradeRequest(@Positive double cutOffGrade) {}
