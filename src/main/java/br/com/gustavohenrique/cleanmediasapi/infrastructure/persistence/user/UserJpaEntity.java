package br.com.gustavohenrique.cleanmediasapi.infrastructure.persistence.user;

import br.com.gustavohenrique.cleanmediasapi.domain.user.Role;
import br.com.gustavohenrique.cleanmediasapi.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Entidade JPA — existe apenas na infrastructure, nunca no domínio.
// Converte para/de User (domínio) via toDomain() e fromDomain().
// O domínio permanece puro: sem @Entity, sem Lombok, sem Spring.
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public static UserJpaEntity fromDomain(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.id = user.getId();
        entity.name = user.getName();
        entity.email = user.getEmail();
        entity.password = user.getPassword();
        entity.role = user.getRole();
        return entity;
    }

    public User toDomain() {
        return new User(id, name, email, password, role);
    }
}
