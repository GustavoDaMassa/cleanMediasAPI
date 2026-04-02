package br.com.gustavohenrique.cleanmediasapi.presentation.auth;

import br.com.gustavohenrique.cleanmediasapi.application.auth.AuthService;
import br.com.gustavohenrique.cleanmediasapi.application.auth.dto.AuthResponse;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// excludeAutoConfiguration: desabilita o Spring Security no slice de controller.
// O endpoint /authenticate é público — não precisamos testar autenticação aqui,
// apenas o comportamento HTTP do controller (request/response, validação, erros).
// A segurança é testada implicitamente no smoke test de integração.
@WebMvcTest(value = AuthController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean AuthService authService;

    @Test
    void shouldReturn200WithTokenWhenCredentialsValid() throws Exception {
        when(authService.authenticate(any())).thenReturn(new AuthResponse("jwt-token"));

        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "joao@test.com", "password": "123456"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void shouldReturn400WhenBodyInvalid() throws Exception {
        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "nao-e-email", "password": ""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturn400WhenCredentialsInvalid() throws Exception {
        when(authService.authenticate(any())).thenThrow(new BusinessException("Invalid credentials"));

        mockMvc.perform(post("/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email": "joao@test.com", "password": "errada"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }
}
