package br.com.gustavohenrique.cleanmediasapi.domain.user;

// Entidade de domínio pura — sem @Entity, sem Spring, sem Lombok.
// O domínio não conhece nenhum framework. Isso é o coração da Clean Architecture.
public class User {

    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;

    // Construtor completo (usado ao reconstituir do banco)
    public User(Long id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Construtor para novo usuário (id ainda não existe)
    public User(String name, String email, String password, Role role) {
        this(null, name, email, password, role);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
}
