package br.com.gustavohenrique.cleanmediasapi.infrastructure.security;

import br.com.gustavohenrique.cleanmediasapi.application.auth.TokenGenerator;
import br.com.gustavohenrique.cleanmediasapi.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// Implementação da porta TokenGenerator (definida na camada de aplicação).
// A dependência aponta para dentro (application → domain), nunca para fora.
@Component
@RequiredArgsConstructor
public class JwtTokenGenerator implements TokenGenerator {

    private final JwtService jwtService;

    @Override
    public String generate(User user) {
        return jwtService.generateToken(user.getEmail(), user.getRole().name());
    }
}
