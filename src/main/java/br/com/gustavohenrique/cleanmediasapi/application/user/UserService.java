package br.com.gustavohenrique.cleanmediasapi.application.user;

import br.com.gustavohenrique.cleanmediasapi.application.user.dto.*;

import java.util.List;

public interface UserService {

    UserResponse create(CreateUserRequest request);

    UserResponse createAdmin(CreateUserRequest request);

    UserResponse findByEmail(String email);

    List<UserResponse> listAll();

    UserResponse updateName(Long id, UpdateNameRequest request);

    UserResponse updateEmail(Long id, UpdateEmailRequest request);

    void delete(Long id);
}
