package br.com.gustavohenrique.cleanmediasapi.domain.user;

import br.com.gustavohenrique.cleanmediasapi.domain.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }

    public UserNotFoundException(String email) {
        super("User with email " + email + " not found");
    }
}
