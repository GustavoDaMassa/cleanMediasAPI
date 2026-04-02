# cleanMediasAPI — Mapa de Classes

Reimplementação da MediasAPI com Clean Architecture (fins educacionais).
Stack: Java 21 · Spring Boot 3.5 · MySQL · Flyway · JWT RSA · Clean Architecture

---

<details id="dir-root">
<summary><strong>/ (raiz)</strong></summary>
<blockquote>

- [pom.xml](pom.xml) — dependências Maven, Java 21, Spring Boot 3.5
- [docker-compose.yml](docker-compose.yml) — MySQL 8.4 local
- [.env.example](.env.example) — variáveis de ambiente necessárias
- [.gitignore](.gitignore)
- [classes.md](classes.md) — este mapa

</blockquote>
</details>

---

## src/main

<details id="dir-root-main">
<summary><strong>cleanmediasapi/</strong></summary>
<blockquote>

<details id="CleanMediasApiApplication">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/CleanMediasApiApplication.java">CleanMediasApiApplication.java</a> [@SpringBootApplication]</strong></summary>
<blockquote>

**metodos**
- `main(String[] args)` — entry point

</blockquote>
</details>

---

### domain/

<details id="dir-domain-exception">
<summary><strong>domain/exception/</strong></summary>
<blockquote>

<details id="NotFoundException">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/exception/NotFoundException.java">NotFoundException.java</a></strong></summary>
<blockquote>

**extends** `RuntimeException` (abstract)

**metodos**
- `NotFoundException(String message)` — protected

</blockquote>
</details>

<details id="BusinessException">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/exception/BusinessException.java">BusinessException.java</a></strong></summary>
<blockquote>

**extends** `RuntimeException`

**metodos**
- `BusinessException(String message)`

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-domain-user">
<summary><strong>domain/user/</strong></summary>
<blockquote>

<details id="Role">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/user/Role.java">Role.java</a></strong></summary>
<blockquote>

**tipo** `enum`

**valores** `ADMIN`, `USER`

</blockquote>
</details>

<details id="User">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/user/User.java">User.java</a></strong></summary>
<blockquote>

Entidade de domínio pura — sem `@Entity`, sem Spring, sem Lombok.

**atributos**
- `Long id` — final
- `String name` — final
- `String email` — final
- `String password` — final
- [`Role`](#Role) `role` — final

**metodos**
- `User(Long id, String name, String email, String password, Role role)` — construtor completo (reconstituição do banco)
- `User(String name, String email, String password, Role role)` — novo usuário (id null)
- getters para todos os campos

</blockquote>
</details>

<details id="UserRepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/user/UserRepository.java">UserRepository.java</a></strong></summary>
<blockquote>

Porta de saída do domínio — implementada pela infrastructure.

**metodos**
- `save(User user): User`
- `findById(Long id): Optional<User>`
- `findByEmail(String email): Optional<User>`
- `findAll(): List<User>`
- `deleteById(Long id): void`
- `existsByEmail(String email): boolean`

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-domain-course">
<summary><strong>domain/course/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

<details id="dir-domain-projection">
<summary><strong>domain/projection/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

<details id="dir-domain-assessment">
<summary><strong>domain/assessment/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

<details id="dir-domain-formula">
<summary><strong>domain/formula/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

---

### application/

<details id="dir-application-auth">
<summary><strong>application/auth/</strong></summary>
<blockquote>

<details id="TokenGenerator">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/auth/TokenGenerator.java">TokenGenerator.java</a></strong></summary>
<blockquote>

Porta de saída — implementada por [`JwtTokenGenerator`](#JwtTokenGenerator).

**metodos**
- `generate(User user): String`

</blockquote>
</details>

<details id="AuthService">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/auth/AuthService.java">AuthService.java</a></strong></summary>
<blockquote>

**metodos**
- `authenticate(AuthRequest request): AuthResponse`

</blockquote>
</details>

<details id="AuthServiceImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/auth/AuthServiceImpl.java">AuthServiceImpl.java</a> [@Service]</strong></summary>
<blockquote>

**implements** [`AuthService`](#AuthService)

**dependencias**
- [`UserRepository`](#UserRepository)
- `PasswordEncoder`
- [`TokenGenerator`](#TokenGenerator)

**metodos**
- `authenticate(AuthRequest request): AuthResponse` — valida email + senha, lança `BusinessException("Invalid credentials")` se inválido

</blockquote>
</details>

<details id="dir-application-auth-dto">
<summary><strong>application/auth/dto/</strong></summary>
<blockquote>

<details id="AuthRequest">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/auth/dto/AuthRequest.java">AuthRequest.java</a></strong></summary>
<blockquote>

**tipo** `record`

**campos** `@NotBlank @Email String email`, `@NotBlank String password`

</blockquote>
</details>

<details id="AuthResponse">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/auth/dto/AuthResponse.java">AuthResponse.java</a></strong></summary>
<blockquote>

**tipo** `record`

**campos** `String token`

</blockquote>
</details>

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-application-user">
<summary><strong>application/user/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

<details id="dir-application-course">
<summary><strong>application/course/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

<details id="dir-application-projection">
<summary><strong>application/projection/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

<details id="dir-application-assessment">
<summary><strong>application/assessment/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

---

### infrastructure/

<details id="dir-infra-config">
<summary><strong>infrastructure/config/</strong></summary>
<blockquote>

- [ModelMapperConfig.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/config/ModelMapperConfig.java) — `@Bean ModelMapper`
- [CorsConfig.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/config/CorsConfig.java) — CORS via `cors.allowed-origins`
- [SwaggerConfig.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/config/SwaggerConfig.java) — OpenAPI 3 com Bearer JWT

</blockquote>
</details>

<details id="dir-infra-security">
<summary><strong>infrastructure/security/</strong></summary>
<blockquote>

<details id="JwtProperties">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/JwtProperties.java">JwtProperties.java</a> [@ConfigurationProperties("jwt")]</strong></summary>
<blockquote>

**tipo** `record`

**campos** `String publicKey`, `String privateKey`, `long expirationSeconds`

</blockquote>
</details>

<details id="JwtService">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/JwtService.java">JwtService.java</a> [@Service]</strong></summary>
<blockquote>

**dependencias** [`JwtProperties`](#JwtProperties)

**metodos**
- `generateToken(String subject, String role): String` — gera JWT RS256 com claim `roles`
- `decoder(): JwtDecoder` — exposto para o `SecurityConfig`
- `derFromPem(String base64Pem): byte[]` — private, extrai DER do PEM em base64

</blockquote>
</details>

<details id="JwtTokenGenerator">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/JwtTokenGenerator.java">JwtTokenGenerator.java</a> [@Component]</strong></summary>
<blockquote>

**implements** [`TokenGenerator`](#TokenGenerator)

**dependencias** [`JwtService`](#JwtService)

**metodos**
- `generate(User user): String`

</blockquote>
</details>

<details id="SecurityConfig">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/SecurityConfig.java">SecurityConfig.java</a> [@Configuration @EnableWebSecurity @EnableMethodSecurity]</strong></summary>
<blockquote>

**dependencias** [`JwtService`](#JwtService)

**beans**
- `filterChain(HttpSecurity): SecurityFilterChain` — stateless, JWT RS256, permissões por endpoint
- `passwordEncoder(): PasswordEncoder` — BCrypt

</blockquote>
</details>

<details id="MdcFilter">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/MdcFilter.java">MdcFilter.java</a> [@Component]</strong></summary>
<blockquote>

**extends** `OncePerRequestFilter`

Adiciona `userEmail` ao MDC para rastreabilidade nos logs.

</blockquote>
</details>

<details id="UserDetailsServiceImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/UserDetailsServiceImpl.java">UserDetailsServiceImpl.java</a> [@Service]</strong></summary>
<blockquote>

**implements** `UserDetailsService`

**dependencias** [`UserRepository`](#UserRepository)

Ponte entre [`User`](#User) (domínio puro) e `UserDetails` (Spring Security).

**metodos**
- `loadUserByUsername(String email): UserDetails`

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-infra-persistence-user">
<summary><strong>infrastructure/persistence/user/</strong></summary>
<blockquote>

<details id="UserJpaEntity">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/user/UserJpaEntity.java">UserJpaEntity.java</a> [@Entity @Table("users")]</strong></summary>
<blockquote>

**atributos**
- `Long id` — `@Id @GeneratedValue`
- `String name`, `String email` (unique), `String password`
- [`Role`](#Role) `role` — `@Enumerated(STRING)`

**metodos**
- `fromDomain(User user): UserJpaEntity` — static
- `toDomain(): User`

</blockquote>
</details>

<details id="UserJpaRepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/user/UserJpaRepository.java">UserJpaRepository.java</a></strong></summary>
<blockquote>

**extends** `JpaRepository<UserJpaEntity, Long>` — package-private

**metodos**
- `findByEmail(String email): Optional<UserJpaEntity>`
- `existsByEmail(String email): boolean`

</blockquote>
</details>

<details id="UserRepositoryImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/user/UserRepositoryImpl.java">UserRepositoryImpl.java</a> [@Repository]</strong></summary>
<blockquote>

**implements** [`UserRepository`](#UserRepository)

**dependencias** [`UserJpaRepository`](#UserJpaRepository)

Adaptador: traduz entre [`User`](#User) (domínio) e [`UserJpaEntity`](#UserJpaEntity) (JPA).

</blockquote>
</details>

</blockquote>
</details>

---

### presentation/

<details id="dir-presentation-exception">
<summary><strong>presentation/exception/</strong></summary>
<blockquote>

<details id="ErrorResponse">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/exception/ErrorResponse.java">ErrorResponse.java</a></strong></summary>
<blockquote>

**tipo** `record`

**campos** `int status`, `String error`, `String message`, `String timestamp`

</blockquote>
</details>

<details id="GlobalExceptionHandler">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/exception/GlobalExceptionHandler.java">GlobalExceptionHandler.java</a> [@RestControllerAdvice]</strong></summary>
<blockquote>

| Exceção | Status |
|---|---|
| [`NotFoundException`](#NotFoundException) | 404 |
| [`BusinessException`](#BusinessException) | 400 |
| `AccessDeniedException` | 403 |
| `MethodArgumentNotValidException` | 400 |

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-presentation-auth">
<summary><strong>presentation/auth/</strong></summary>
<blockquote>

<details id="AuthController">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/auth/AuthController.java">AuthController.java</a> [@RestController @RequestMapping("/authenticate")]</strong></summary>
<blockquote>

**dependencias** [`AuthService`](#AuthService)

**endpoints**
- `POST /authenticate` → [`AuthResponse`](#AuthResponse) (200)

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-presentation-user">
<summary><strong>presentation/user/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

<details id="dir-presentation-course">
<summary><strong>presentation/course/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

<details id="dir-presentation-projection">
<summary><strong>presentation/projection/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

<details id="dir-presentation-assessment">
<summary><strong>presentation/assessment/</strong></summary>
<blockquote>

_vazio — Fase 5_

</blockquote>
</details>

</blockquote>
</details>

---

## src/main/resources

- [application.properties](src/main/resources/application.properties) — configuração principal
- [db/migration/](src/main/resources/db/migration/) — scripts Flyway (Fase 6)

---

## src/test

| Classe de Teste | Tipo | Cobertura |
|---|---|---|
| [UserTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/domain/user/UserTest.java) | JUnit puro | construtores e getters de `User` |
| [AuthServiceImplTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/application/auth/AuthServiceImplTest.java) | Mockito | fluxo de autenticação (sucesso e falhas) |
| [JwtServiceTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/JwtServiceTest.java) | JUnit puro | geração e decodificação de JWT |
| [UserRepositoryImplTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/user/UserRepositoryImplTest.java) | @DataJpaTest + H2 | CRUD e queries do repositório |
| [GlobalExceptionHandlerTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/presentation/exception/GlobalExceptionHandlerTest.java) | @WebMvcTest | mapeamento de exceções → status HTTP |
| [AuthControllerTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/presentation/auth/AuthControllerTest.java) | @WebMvcTest | endpoints HTTP do controller de auth |
