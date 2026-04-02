package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.user;

import br.com.gustavohenrique.cleanmediasapi.domain.user.Role;
import br.com.gustavohenrique.cleanmediasapi.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest sobe apenas a camada JPA com H2 em memória.
// @Import traz o UserRepositoryImpl (que não é detectado automaticamente pelo slice).
@DataJpaTest
@Import(UserRepositoryImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    void shouldSaveAndFindUserById() {
        User saved = userRepository.save(new User("João", "joao@test.com", "hashed", Role.USER));

        assertThat(saved.getId()).isNotNull();

        Optional<User> found = userRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("João");
        assertThat(found.get().getEmail()).isEqualTo("joao@test.com");
        assertThat(found.get().getRole()).isEqualTo(Role.USER);
    }

    @Test
    void shouldFindByEmail() {
        userRepository.save(new User("Maria", "maria@test.com", "hashed", Role.ADMIN));

        Optional<User> found = userRepository.findByEmail("maria@test.com");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Maria");
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound() {
        Optional<User> found = userRepository.findByEmail("naoexiste@test.com");
        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        userRepository.save(new User("Pedro", "pedro@test.com", "hashed", Role.USER));

        assertThat(userRepository.existsByEmail("pedro@test.com")).isTrue();
        assertThat(userRepository.existsByEmail("outro@test.com")).isFalse();
    }
}
