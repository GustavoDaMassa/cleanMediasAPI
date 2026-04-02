package br.com.gustavohenrique.cleanmediasapi.domain.user;

import java.util.List;
import java.util.Optional;

// Porta de saída do domínio (interface definida pelo domínio, implementada pela infrastructure).
// O domínio declara o contrato; o JPA/MySQL são detalhes que ficam fora.
public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    void deleteById(Long id);

    boolean existsByEmail(String email);
}
