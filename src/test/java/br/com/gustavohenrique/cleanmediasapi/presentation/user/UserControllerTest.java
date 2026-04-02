package br.com.gustavohenrique.cleanmediasapi.presentation.user;

import br.com.gustavohenrique.cleanmediasapi.application.user.UserService;
import br.com.gustavohenrique.cleanmediasapi.application.user.dto.UserResponse;
import br.com.gustavohenrique.cleanmediasapi.domain.exception.BusinessException;
import br.com.gustavohenrique.cleanmediasapi.domain.user.Role;
import br.com.gustavohenrique.cleanmediasapi.domain.user.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean UserService userService;

    private final UserResponse userResponse = new UserResponse(1L, "João", "joao@test.com", Role.USER);
    private final UserResponse adminResponse = new UserResponse(2L, "Admin", "admin@test.com", Role.ADMIN);

    // --- POST /api/v1/users ---

    @Test
    void shouldReturn201WhenUserCreated() throws Exception {
        when(userService.create(any())).thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"João","email":"joao@test.com","password":"123456"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("joao@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void shouldReturn400WhenCreateUserBodyInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"","email":"nao-e-email","password":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturn400WhenEmailAlreadyInUse() throws Exception {
        when(userService.create(any())).thenThrow(new BusinessException("Email already in use"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"João","email":"joao@test.com","password":"123456"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email already in use"));
    }

    // --- POST /api/v1/users/admin ---

    @Test
    void shouldReturn201WhenAdminCreated() throws Exception {
        when(userService.createAdmin(any())).thenReturn(adminResponse);

        mockMvc.perform(post("/api/v1/users/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Admin","email":"admin@test.com","password":"123456"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    // --- GET /api/v1/users ---

    @Test
    void shouldReturn200WithUserList() throws Exception {
        when(userService.listAll()).thenReturn(List.of(userResponse, adminResponse));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // --- GET /api/v1/users/{email} ---

    @Test
    void shouldReturn200WhenUserFoundByEmail() throws Exception {
        when(userService.findByEmail("joao@test.com")).thenReturn(userResponse);

        mockMvc.perform(get("/api/v1/users/joao@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("joao@test.com"));
    }

    @Test
    void shouldReturn404WhenUserNotFoundByEmail() throws Exception {
        when(userService.findByEmail("x@test.com")).thenThrow(new UserNotFoundException("x@test.com"));

        mockMvc.perform(get("/api/v1/users/x@test.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // --- PATCH /api/v1/users/{id}/name ---

    @Test
    void shouldReturn200WhenNameUpdated() throws Exception {
        UserResponse updated = new UserResponse(1L, "João Novo", "joao@test.com", Role.USER);
        when(userService.updateName(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/users/1/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"João Novo"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("João Novo"));
    }

    @Test
    void shouldReturn404WhenUserNotFoundOnUpdateName() throws Exception {
        when(userService.updateName(eq(99L), any())).thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(patch("/api/v1/users/99/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Novo"}
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // --- PATCH /api/v1/users/{id}/email ---

    @Test
    void shouldReturn200WhenEmailUpdated() throws Exception {
        UserResponse updated = new UserResponse(1L, "João", "novo@test.com", Role.USER);
        when(userService.updateEmail(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/users/1/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"novo@test.com"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("novo@test.com"));
    }

    // --- DELETE /api/v1/users/{id} ---

    @Test
    void shouldReturn204WhenUserDeleted() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenUserNotFoundOnDelete() throws Exception {
        doThrow(new UserNotFoundException(99L)).when(userService).delete(99L);

        mockMvc.perform(delete("/api/v1/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
