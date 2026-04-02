package br.com.gustavohenrique.cleanmediasapi.domain.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// Teste puro JUnit — sem Spring, sem mocks. Valida os invariantes da entidade de domínio.
class UserTest {

    @Test
    void shouldCreateUserWithAllFields() {
        User user = new User(1L, "João", "joao@test.com", "hashed", Role.USER);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("João");
        assertThat(user.getEmail()).isEqualTo("joao@test.com");
        assertThat(user.getPassword()).isEqualTo("hashed");
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void shouldCreateNewUserWithoutId() {
        User user = new User("Maria", "maria@test.com", "hashed", Role.ADMIN);

        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("Maria");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
    }
}
