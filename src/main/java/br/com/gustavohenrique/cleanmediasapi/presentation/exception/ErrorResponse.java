package br.com.gustavohenrique.cleanmediasapi.presentation.exception;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String timestamp
) {}
