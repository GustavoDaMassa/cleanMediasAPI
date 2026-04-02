package br.com.gustavohenrique.cleanmediasapi.domain.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
