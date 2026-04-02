package br.com.gustavohenrique.cleanmediasapi.application.auth;

import br.com.gustavohenrique.cleanmediasapi.application.auth.dto.AuthRequest;
import br.com.gustavohenrique.cleanmediasapi.application.auth.dto.AuthResponse;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import br.com.gustavohenrique.cleanmediasapi.domain.user.Role;
import br.com.gustavohenrique.cleanmediasapi.domain.user.User;
import br.com.gustavohenrique.cleanmediasapi.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock TokenGenerator tokenGenerator;
    @InjectMocks AuthServiceImpl authService;

    private final User user = new User(1L, "João", "joao@test.com", "hashed_pass", Role.USER);

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() {
        when(userRepository.findByEmail("joao@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "hashed_pass")).thenReturn(true);
        when(tokenGenerator.generate(user)).thenReturn("jwt-token");

        AuthResponse response = authService.authenticate(new AuthRequest("joao@test.com", "123456"));

        assertThat(response.token()).isEqualTo("jwt-token");
    }

    @Test
    void shouldThrowBusinessExceptionWhenEmailNotFound() {
        when(userRepository.findByEmail("naoexiste@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.authenticate(new AuthRequest("naoexiste@test.com", "123")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Invalid credentials");
    }

    @Test
    void shouldThrowBusinessExceptionWhenPasswordWrong() {
        when(userRepository.findByEmail("joao@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha_errada", "hashed_pass")).thenReturn(false);

        assertThatThrownBy(() -> authService.authenticate(new AuthRequest("joao@test.com", "senha_errada")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Invalid credentials");
    }
}
