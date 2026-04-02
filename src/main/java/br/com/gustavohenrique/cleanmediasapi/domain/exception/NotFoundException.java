package br.com.gustavohenrique.cleanmediasapi.domain.exception;

public abstract class NotFoundException extends RuntimeException {

    protected NotFoundException(String message) {
        super(message);
    }
}
