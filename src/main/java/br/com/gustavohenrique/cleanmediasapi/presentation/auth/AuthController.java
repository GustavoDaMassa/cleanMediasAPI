package br.com.gustavohenrique.cleanmediasapi.presentation.auth;

import br.com.gustavohenrique.cleanmediasapi.application.auth.AuthService;
import br.com.gustavohenrique.cleanmediasapi.application.auth.dto.AuthRequest;
import br.com.gustavohenrique.cleanmediasapi.application.auth.dto.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
