package br.com.gustavohenrique.cleanmediasapi.application.user.dto;

import br.com.gustavohenrique.cleanmediasapi.domain.user.Role;
import br.com.gustavohenrique.cleanmediasapi.domain.user.User;

public record UserResponse(Long id, String name, String email, Role role) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
