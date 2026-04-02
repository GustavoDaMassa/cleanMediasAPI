package br.com.gustavohenrique.cleanmediasapi.application.auth;

import br.com.gustavohenrique.cleanmediasapi.application.auth.dto.AuthRequest;
import br.com.gustavohenrique.cleanmediasapi.application.auth.dto.AuthResponse;

public interface AuthService {

    AuthResponse authenticate(AuthRequest request);
}
