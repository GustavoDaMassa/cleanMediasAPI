package br.com.gustavohenrique.cleanmediasapi.application.user;

import br.com.gustavohenrique.cleanmediasapi.application.user.dto.*;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import br.com.gustavohenrique.cleanmediasapi.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(CreateUserRequest request) {
        return save(request, Role.USER);
    }

    @Override
    public UserResponse createAdmin(CreateUserRequest request) {
        return save(request, Role.ADMIN);
    }

    @Override
    public UserResponse findByEmail(String email) {
        return UserResponse.from(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException(email))
        );
    }

    @Override
    public List<UserResponse> listAll() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @Override
    public UserResponse updateName(Long id, UpdateNameRequest request) {
        User existing = requireExists(id);
        User updated = new User(existing.getId(), request.name(), existing.getEmail(),
                existing.getPassword(), existing.getRole());
        return UserResponse.from(userRepository.save(updated));
    }

    @Override
    public UserResponse updateEmail(Long id, UpdateEmailRequest request) {
        User existing = requireExists(id);
        requireEmailAvailable(request.email());
        User updated = new User(existing.getId(), existing.getName(), request.email(),
                existing.getPassword(), existing.getRole());
        return UserResponse.from(userRepository.save(updated));
    }

    @Override
    public void delete(Long id) {
        requireExists(id);
        userRepository.deleteById(id);
    }

    // --- helpers privados ---

    private UserResponse save(CreateUserRequest request, Role role) {
        requireEmailAvailable(request.email());
        User user = new User(request.name(), request.email(),
                passwordEncoder.encode(request.password()), role);
        return UserResponse.from(userRepository.save(user));
    }

    private User requireExists(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private void requireEmailAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Email already in use");
        }
    }
}
