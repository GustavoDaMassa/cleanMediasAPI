package br.com.gustavohenrique.cleanmediasapi.application.auth;

import br.com.gustavohenrique.cleanmediasapi.application.auth.dto.AuthRequest;
import br.com.gustavohenrique.cleanmediasapi.application.auth.dto.AuthResponse;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import br.com.gustavohenrique.cleanmediasapi.domain.user.User;
import br.com.gustavohenrique.cleanmediasapi.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException("Invalid credentials");
        }

        return new AuthResponse(tokenGenerator.generate(user));
    }
}
