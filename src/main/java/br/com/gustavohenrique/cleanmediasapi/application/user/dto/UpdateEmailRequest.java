package br.com.gustavohenrique.cleanmediasapi.application.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateEmailRequest(@NotBlank @Email String email) {}
