package br.com.gustavohenrique.cleanmediasapi.application.user;

import br.com.gustavohenrique.cleanmediasapi.application.user.dto.*;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import br.com.gustavohenrique.cleanmediasapi.domain.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks UserServiceImpl userService;

    private final User user = new User(1L, "João", "joao@test.com", "hashed", Role.USER);

    // --- create ---

    @Test
    void shouldCreateUserWithRoleUser() {
        when(userRepository.existsByEmail("joao@test.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hashed");
        when(userRepository.save(any())).thenReturn(user);

        UserResponse response = userService.create(new CreateUserRequest("João", "joao@test.com", "123456"));

        assertThat(response.email()).isEqualTo("joao@test.com");
        assertThat(response.role()).isEqualTo(Role.USER);
    }

    @Test
    void shouldCreateUserWithRoleAdmin() {
        User admin = new User(2L, "Admin", "admin@test.com", "hashed", Role.ADMIN);
        when(userRepository.existsByEmail("admin@test.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("hashed");
        when(userRepository.save(any())).thenReturn(admin);

        UserResponse response = userService.createAdmin(new CreateUserRequest("Admin", "admin@test.com", "123456"));

        assertThat(response.role()).isEqualTo(Role.ADMIN);
    }

    @Test
    void shouldThrowWhenEmailAlreadyExistsOnCreate() {
        when(userRepository.existsByEmail("joao@test.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(new CreateUserRequest("João", "joao@test.com", "123")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email already in use");
    }

    // --- findByEmail ---

    @Test
    void shouldFindUserByEmail() {
        when(userRepository.findByEmail("joao@test.com")).thenReturn(Optional.of(user));

        UserResponse response = userService.findByEmail("joao@test.com");

        assertThat(response.email()).isEqualTo("joao@test.com");
    }

    @Test
    void shouldThrowWhenEmailNotFound() {
        when(userRepository.findByEmail("x@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail("x@test.com"))
                .isInstanceOf(UserNotFoundException.class);
    }

    // --- listAll ---

    @Test
    void shouldReturnAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> result = userService.listAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).email()).isEqualTo("joao@test.com");
    }

    // --- updateName ---

    @Test
    void shouldUpdateUserName() {
        User updated = new User(1L, "João Novo", "joao@test.com", "hashed", Role.USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(updated);

        UserResponse response = userService.updateName(1L, new UpdateNameRequest("João Novo"));

        assertThat(response.name()).isEqualTo("João Novo");
    }

    @Test
    void shouldThrowWhenUserNotFoundOnUpdateName() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateName(99L, new UpdateNameRequest("Novo")))
                .isInstanceOf(UserNotFoundException.class);
    }

    // --- updateEmail ---

    @Test
    void shouldUpdateUserEmail() {
        User updated = new User(1L, "João", "novo@test.com", "hashed", Role.USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("novo@test.com")).thenReturn(false);
        when(userRepository.save(any())).thenReturn(updated);

        UserResponse response = userService.updateEmail(1L, new UpdateEmailRequest("novo@test.com"));

        assertThat(response.email()).isEqualTo("novo@test.com");
    }

    @Test
    void shouldThrowWhenNewEmailAlreadyInUseOnUpdate() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("outro@test.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateEmail(1L, new UpdateEmailRequest("outro@test.com")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email already in use");
    }

    // --- delete ---

    @Test
    void shouldDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenUserNotFoundOnDelete() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
