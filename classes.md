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
- `User(Long id, String name, String email, String password, Role role)` — reconstituição do banco
- `User(String name, String email, String password, Role role)` — novo usuário (id null)
- getters para todos os campos

</blockquote>
</details>

<details id="UserRepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/user/UserRepository.java">UserRepository.java</a></strong></summary>
<blockquote>

Porta de saída do domínio — implementada pela infrastructure.

**metodos**
- `save(User): User`
- `findById(Long): Optional<User>`
- `findByEmail(String): Optional<User>`
- `findAll(): List<User>`
- `deleteById(Long): void`
- `existsByEmail(String): boolean`

</blockquote>
</details>

<details id="UserNotFoundException">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/user/UserNotFoundException.java">UserNotFoundException.java</a></strong></summary>
<blockquote>

**extends** [`NotFoundException`](#NotFoundException)

**metodos**
- `UserNotFoundException(Long id)`
- `UserNotFoundException(String email)`

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-domain-course">
<summary><strong>domain/course/</strong></summary>
<blockquote>

<details id="Course">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/course/Course.java">Course.java</a></strong></summary>
<blockquote>

Entidade de domínio pura.

**atributos**
- `Long id`, `Long userId`, `String name`, `String averageMethod`, `double cutOffGrade` — todos final

**metodos**
- `Course(Long id, Long userId, String name, String averageMethod, double cutOffGrade)` — reconstituição
- `Course(Long userId, String name, String averageMethod, double cutOffGrade)` — novo curso (id null)
- getters para todos os campos

</blockquote>
</details>

<details id="CourseRepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/course/CourseRepository.java">CourseRepository.java</a></strong></summary>
<blockquote>

**metodos**
- `save(Course): Course`
- `findByIdAndUserId(Long, Long): Optional<Course>`
- `findAllByUserId(Long): List<Course>`
- `deleteByIdAndUserId(Long, Long): void`
- `existsByNameAndUserId(String, Long): boolean`

</blockquote>
</details>

<details id="CourseNotFoundException">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/course/CourseNotFoundException.java">CourseNotFoundException.java</a></strong></summary>
<blockquote>

**extends** [`NotFoundException`](#NotFoundException)

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-domain-projection">
<summary><strong>domain/projection/</strong></summary>
<blockquote>

<details id="Projection">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/projection/Projection.java">Projection.java</a></strong></summary>
<blockquote>

Entidade de domínio pura.

**atributos**
- `Long id`, `Long courseId`, `String name`, `double finalGrade` — todos final

**metodos**
- `Projection(Long id, Long courseId, String name, double finalGrade)` — reconstituição
- `Projection(Long courseId, String name)` — nova projeção (id null, finalGrade=0.0)
- getters para todos os campos

</blockquote>
</details>

<details id="ProjectionRepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/projection/ProjectionRepository.java">ProjectionRepository.java</a></strong></summary>
<blockquote>

**metodos**
- `save(Projection): Projection`
- `findById(Long): Optional<Projection>`
- `findByIdAndCourseId(Long, Long): Optional<Projection>`
- `findAllByCourseId(Long): List<Projection>`
- `findAllByUserId(Long): List<Projection>`
- `deleteByIdAndCourseId(Long, Long): void`
- `deleteAllByCourseId(Long): void`
- `existsByNameAndCourseId(String, Long): boolean`

</blockquote>
</details>

<details id="ProjectionNotFoundException">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/projection/ProjectionNotFoundException.java">ProjectionNotFoundException.java</a></strong></summary>
<blockquote>

**extends** [`NotFoundException`](#NotFoundException)

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-domain-assessment">
<summary><strong>domain/assessment/</strong></summary>
<blockquote>

<details id="Assessment">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/assessment/Assessment.java">Assessment.java</a></strong></summary>
<blockquote>

Entidade com invariante central: `grade` sem setter público — só acessível via `applyGrade()`.

**atributos**
- `Long id`, `Long projectionId`, `String identifier`, `double maxValue` — final
- `double grade`, `double requiredGrade`, `boolean fixed` — mutáveis apenas por métodos específicos

**metodos**
- `Assessment(Long id, Long projectionId, String identifier, double grade, double maxValue, double requiredGrade, boolean fixed)` — reconstituição
- `Assessment(Long projectionId, String identifier, double maxValue)` — novo assessment (grade=0, fixed=false)
- `applyGrade(double grade)` — valida `0 <= grade <= maxValue`, seta `fixed=true`; lança [`BusinessException`](#BusinessException) se inválido
- `updateRequiredGrade(double requiredGrade)`
- getters para todos os campos

</blockquote>
</details>

<details id="AssessmentRepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/assessment/AssessmentRepository.java">AssessmentRepository.java</a></strong></summary>
<blockquote>

**metodos**
- `save(Assessment): Assessment`
- `saveAll(List<Assessment>): List<Assessment>`
- `findByIdAndProjectionId(Long, Long): Optional<Assessment>`
- `findAllByProjectionId(Long): List<Assessment>`
- `existsByIdentifierAndProjectionId(String, Long): boolean`
- `deleteAllByProjectionId(Long): void`

</blockquote>
</details>

<details id="AssessmentNotFoundException">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/assessment/AssessmentNotFoundException.java">AssessmentNotFoundException.java</a></strong></summary>
<blockquote>

**extends** [`NotFoundException`](#NotFoundException)

**metodos**
- `AssessmentNotFoundException(Long id)`
- `AssessmentNotFoundException(Long id, Long projectionId)`

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-domain-formula">
<summary><strong>domain/formula/</strong></summary>
<blockquote>

Pipeline de avaliação de fórmulas matemáticas: `FormulaParser` → `ShuntingYard` → `RpnEvaluator`.

<details id="FormulaToken">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/formula/FormulaToken.java">FormulaToken.java</a></strong></summary>
<blockquote>

**tipo** `record`

**campos** `Type type`, `String value`

**tipos** `NUMBER`, `IDENTIFIER`, `OPERATOR`, `FUNCTION` (`@M[n]`), `LPAREN`, `RPAREN`, `SEP` (`;`)

**metodos** `isNumber()`, `isIdentifier()`, `isOperator()`, `isFunction()`, `isLParen()`, `isRParen()`, `isSep()`

</blockquote>
</details>

<details id="FormulaParser">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/formula/FormulaParser.java">FormulaParser.java</a></strong></summary>
<blockquote>

**metodos**
- `extractIdentifiers(String formula): Map<String, Double>` — extrai identificadores com seus `maxValue` (padrão 10.0); suporta `AV1[8]`
- `tokenize(String formula): List<FormulaToken>` — transforma a string em lista de tokens via regex multi-grupo

</blockquote>
</details>

<details id="ShuntingYard">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/formula/ShuntingYard.java">ShuntingYard.java</a></strong></summary>
<blockquote>

Algoritmo de Dijkstra — converte tokens infixos para RPN.

**metodos**
- `toRpn(List<FormulaToken>): List<FormulaToken>` — rastreia `argCountStack` para anotar funções como `@M[n]:argCount`

</blockquote>
</details>

<details id="RpnEvaluator">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/domain/formula/RpnEvaluator.java">RpnEvaluator.java</a></strong></summary>
<blockquote>

Avaliador de pilha para RPN.

**metodos**
- `evaluate(List<FormulaToken> rpn, Function<String, Double> resolver): double` — números/identificadores empilhados, operadores consomem 2, `@M[n]:argCount` consome exatamente `argCount` valores, ordena decrescente, soma top-n, divide por n

</blockquote>
</details>

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
- `authenticate(AuthRequest): AuthResponse`

</blockquote>
</details>

<details id="AuthServiceImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/auth/AuthServiceImpl.java">AuthServiceImpl.java</a> [@Service]</strong></summary>
<blockquote>

**implements** [`AuthService`](#AuthService)

**dependencias** [`UserRepository`](#UserRepository) · `PasswordEncoder` · [`TokenGenerator`](#TokenGenerator)

**metodos**
- `authenticate(AuthRequest): AuthResponse` — valida email + senha, lança `BusinessException("Invalid credentials")` se inválido

</blockquote>
</details>

<details id="dir-application-auth-dto">
<summary><strong>application/auth/dto/</strong></summary>
<blockquote>

- [AuthRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/auth/dto/AuthRequest.java) — record: `@NotBlank @Email email`, `@NotBlank password`
- [AuthResponse.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/auth/dto/AuthResponse.java) — record: `String token`

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-application-user">
<summary><strong>application/user/</strong></summary>
<blockquote>

<details id="UserService">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/user/UserService.java">UserService.java</a></strong></summary>
<blockquote>

**metodos**
- `create(CreateUserRequest): UserResponse`
- `createAdmin(CreateUserRequest): UserResponse`
- `findByEmail(String): UserResponse`
- `listAll(): List<UserResponse>`
- `updateName(Long, UpdateNameRequest): UserResponse`
- `updateEmail(Long, UpdateEmailRequest): UserResponse`
- `delete(Long): void`

</blockquote>
</details>

<details id="UserServiceImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/user/UserServiceImpl.java">UserServiceImpl.java</a> [@Service]</strong></summary>
<blockquote>

**implements** [`UserService`](#UserService)

**dependencias** [`UserRepository`](#UserRepository) · `PasswordEncoder`

**regras de negócio**
- email único → `BusinessException("Email already in use")`
- usuário deve existir → [`UserNotFoundException`](#UserNotFoundException)

</blockquote>
</details>

<details id="dir-application-user-dto">
<summary><strong>application/user/dto/</strong></summary>
<blockquote>

- [CreateUserRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/user/dto/CreateUserRequest.java) — record: `name`, `@Email email`, `password`
- [UpdateNameRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/user/dto/UpdateNameRequest.java) — record: `name`
- [UpdateEmailRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/user/dto/UpdateEmailRequest.java) — record: `@Email email`
- [UserResponse.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/user/dto/UserResponse.java) — record: `id`, `name`, `email`, `role` + `from(User)`

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-application-course">
<summary><strong>application/course/</strong></summary>
<blockquote>

<details id="CourseService">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/course/CourseService.java">CourseService.java</a></strong></summary>
<blockquote>

**metodos**
- `create(Long userId, CreateCourseRequest): CourseResponse`
- `listAll(Long userId): List<CourseResponse>`
- `updateName(Long userId, Long id, UpdateCourseNameRequest): CourseResponse`
- `updateAverageMethod(Long userId, Long id, UpdateAverageMethodRequest): CourseResponse`
- `updateCutOffGrade(Long userId, Long id, UpdateCutOffGradeRequest): CourseResponse`
- `delete(Long userId, Long id): void`
- `findByIdAndUserId(Long userId, Long id): CourseResponse`

</blockquote>
</details>

<details id="CourseServiceImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/course/CourseServiceImpl.java">CourseServiceImpl.java</a> [@Service]</strong></summary>
<blockquote>

**implements** [`CourseService`](#CourseService)

**dependencias** [`CourseRepository`](#CourseRepository) · [`ProjectionService`](#ProjectionService)

**regras de negócio**
- nome único por usuário → `BusinessException("Course name already in use")`
- ao criar: chama `projectionService.initializeForCourse()`
- ao atualizar `averageMethod`: deleta e recria todas as projeções do curso

</blockquote>
</details>

<details id="dir-application-course-dto">
<summary><strong>application/course/dto/</strong></summary>
<blockquote>

- [CreateCourseRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/course/dto/CreateCourseRequest.java) — record: `name`, `averageMethod`, `cutOffGrade`
- [UpdateCourseNameRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/course/dto/UpdateCourseNameRequest.java) — record: `name`
- [UpdateAverageMethodRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/course/dto/UpdateAverageMethodRequest.java) — record: `averageMethod`
- [UpdateCutOffGradeRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/course/dto/UpdateCutOffGradeRequest.java) — record: `cutOffGrade`
- [CourseResponse.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/course/dto/CourseResponse.java) — record: `id`, `name`, `averageMethod`, `cutOffGrade` + `from(Course)`

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-application-projection">
<summary><strong>application/projection/</strong></summary>
<blockquote>

<details id="ProjectionService">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/projection/ProjectionService.java">ProjectionService.java</a></strong></summary>
<blockquote>

**metodos**
- `initializeForCourse(Course): void` — cria projeção default + assessments para um curso novo
- `deleteAllByCourse(Long courseId): void`
- `create(Course, CreateProjectionRequest): ProjectionResponse`
- `listByCourse(Long courseId): List<ProjectionResponse>`
- `listAllByUser(Long userId): List<ProjectionResponse>`
- `updateName(Long courseId, Long id, UpdateProjectionNameRequest): ProjectionResponse`
- `delete(Long courseId, Long id): void`

</blockquote>
</details>

<details id="ProjectionServiceImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/projection/ProjectionServiceImpl.java">ProjectionServiceImpl.java</a> [@Service]</strong></summary>
<blockquote>

**implements** [`ProjectionService`](#ProjectionService)

**dependencias** [`ProjectionRepository`](#ProjectionRepository) · [`AssessmentService`](#AssessmentService)

**regras de negócio**
- nome único por curso → `BusinessException("Projection name already in use")`
- ao criar: chama `assessmentService.initializeForProjection()`

</blockquote>
</details>

<details id="dir-application-projection-dto">
<summary><strong>application/projection/dto/</strong></summary>
<blockquote>

- [CreateProjectionRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/projection/dto/CreateProjectionRequest.java) — record: `name`
- [UpdateProjectionNameRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/projection/dto/UpdateProjectionNameRequest.java) — record: `name`
- [ProjectionResponse.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/projection/dto/ProjectionResponse.java) — record: `id`, `courseId`, `name`, `finalGrade` + `from(Projection)`

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-application-assessment">
<summary><strong>application/assessment/</strong></summary>
<blockquote>

<details id="AssessmentService">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/assessment/AssessmentService.java">AssessmentService.java</a></strong></summary>
<blockquote>

**metodos**
- `initializeForProjection(Projection, String averageMethod): void` — parseia fórmula e cria assessments
- `deleteAllByProjectionId(Long): void`
- `findAllByProjectionId(Long): List<AssessmentResponse>`
- `findByIdAndProjectionId(Long id, Long projectionId): AssessmentResponse`
- `applyGrade(Long projectionId, Long assessmentId, double grade, String averageMethod): AssessmentResponse`

</blockquote>
</details>

<details id="AssessmentServiceImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/application/assessment/AssessmentServiceImpl.java">AssessmentServiceImpl.java</a> [@Service]</strong></summary>
<blockquote>

**implements** [`AssessmentService`](#AssessmentService)

**dependencias** [`AssessmentRepository`](#AssessmentRepository) · [`ProjectionRepository`](#ProjectionRepository)

**instâncias internas** [`FormulaParser`](#FormulaParser) · [`ShuntingYard`](#ShuntingYard) · [`RpnEvaluator`](#RpnEvaluator)

**regras de negócio**
- `applyGrade()`: valida via `Assessment.applyGrade()`, persiste, recalcula `finalGrade` da projeção via pipeline RPN com notas atuais de todos os assessments

</blockquote>
</details>

<details id="dir-application-assessment-dto">
<summary><strong>application/assessment/dto/</strong></summary>
<blockquote>

- [AssessmentResponse.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/application/assessment/dto/AssessmentResponse.java) — record: `id`, `projectionId`, `identifier`, `maxValue`, `grade`, `requiredGrade`, `fixed` + `from(Assessment)`

</blockquote>
</details>

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
- `decoder(): JwtDecoder`
- `derFromPem(String base64Pem): byte[]` — private; extrai DER do PEM em base64 (strip headers antes de decodificar)

</blockquote>
</details>

<details id="JwtTokenGenerator">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/JwtTokenGenerator.java">JwtTokenGenerator.java</a> [@Component]</strong></summary>
<blockquote>

**implements** [`TokenGenerator`](#TokenGenerator)

**dependencias** [`JwtService`](#JwtService)

</blockquote>
</details>

<details id="SecurityConfig">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/SecurityConfig.java">SecurityConfig.java</a> [@Configuration @EnableWebSecurity @EnableMethodSecurity]</strong></summary>
<blockquote>

**dependencias** [`JwtService`](#JwtService)

**beans**
- `filterChain(HttpSecurity): SecurityFilterChain` — STATELESS, JWT RS256, permissões por endpoint
- `passwordEncoder(): PasswordEncoder` — BCrypt

</blockquote>
</details>

<details id="MdcFilter">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/MdcFilter.java">MdcFilter.java</a> [@Component]</strong></summary>
<blockquote>

**extends** `OncePerRequestFilter` — adiciona `userEmail` ao MDC para rastreabilidade nos logs

</blockquote>
</details>

<details id="UserDetailsServiceImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/UserDetailsServiceImpl.java">UserDetailsServiceImpl.java</a> [@Service]</strong></summary>
<blockquote>

**implements** `UserDetailsService`

**dependencias** [`UserRepository`](#UserRepository)

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

**metodos**
- `fromDomain(User): UserJpaEntity` — static
- `toDomain(): User`

</blockquote>
</details>

<details id="UserJpaRepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/user/UserJpaRepository.java">UserJpaRepository.java</a></strong></summary>
<blockquote>

**extends** `JpaRepository<UserJpaEntity, Long>` — package-private

**metodos** `findByEmail(String)`, `existsByEmail(String)`

</blockquote>
</details>

<details id="UserRepositoryImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/user/UserRepositoryImpl.java">UserRepositoryImpl.java</a> [@Repository]</strong></summary>
<blockquote>

**implements** [`UserRepository`](#UserRepository) · **dependencias** [`UserJpaRepository`](#UserJpaRepository)

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-infra-persistence-course">
<summary><strong>infrastructure/persistence/course/</strong></summary>
<blockquote>

<details id="CourseJpaEntity">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/course/CourseJpaEntity.java">CourseJpaEntity.java</a> [@Entity @Table("courses")]</strong></summary>
<blockquote>

**metodos** `fromDomain(Course): CourseJpaEntity` (static) · `toDomain(): Course`

</blockquote>
</details>

<details id="CourseJpaRepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/course/CourseJpaRepository.java">CourseJpaRepository.java</a></strong></summary>
<blockquote>

**extends** `JpaRepository<CourseJpaEntity, Long>` — package-private

**metodos** `findByIdAndUserId`, `findAllByUserId`, `deleteByIdAndUserId`, `existsByNameAndUserId`

</blockquote>
</details>

<details id="CourseRepositoryImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/course/CourseRepositoryImpl.java">CourseRepositoryImpl.java</a> [@Repository]</strong></summary>
<blockquote>

**implements** [`CourseRepository`](#CourseRepository) · **dependencias** [`CourseJpaRepository`](#CourseJpaRepository)

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-infra-persistence-projection">
<summary><strong>infrastructure/persistence/projection/</strong></summary>
<blockquote>

<details id="ProjectionJpaEntity">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/projection/ProjectionJpaEntity.java">ProjectionJpaEntity.java</a> [@Entity @Table("projections")]</strong></summary>
<blockquote>

**metodos** `fromDomain(Projection): ProjectionJpaEntity` (static) · `toDomain(): Projection`

</blockquote>
</details>

<details id="ProjectionJpaRepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/projection/ProjectionJpaRepository.java">ProjectionJpaRepository.java</a></strong></summary>
<blockquote>

**extends** `JpaRepository<ProjectionJpaEntity, Long>` — package-private

**metodos** `findByIdAndCourseId`, `findAllByCourseId`, `findAllByUserId` (JPQL via join com courses), `deleteByIdAndCourseId`, `deleteAllByCourseId`, `existsByNameAndCourseId`

</blockquote>
</details>

<details id="ProjectionRepositoryImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/projection/ProjectionRepositoryImpl.java">ProjectionRepositoryImpl.java</a> [@Repository]</strong></summary>
<blockquote>

**implements** [`ProjectionRepository`](#ProjectionRepository) · **dependencias** [`ProjectionJpaRepository`](#ProjectionJpaRepository)

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-infra-persistence-assessment">
<summary><strong>infrastructure/persistence/assessment/</strong></summary>
<blockquote>

<details id="AssessmentJpaEntity">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/assessment/AssessmentJpaEntity.java">AssessmentJpaEntity.java</a> [@Entity @Table("assessments")]</strong></summary>
<blockquote>

**metodos** `fromDomain(Assessment): AssessmentJpaEntity` (static) · `toDomain(): Assessment`

</blockquote>
</details>

<details id="AssessmentJpaRepository">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/assessment/AssessmentJpaRepository.java">AssessmentJpaRepository.java</a></strong></summary>
<blockquote>

**extends** `JpaRepository<AssessmentJpaEntity, Long>` — package-private

**metodos** `findByIdAndProjectionId`, `findAllByProjectionId`, `existsByIdentifierAndProjectionId`, `deleteAllByProjectionId`

</blockquote>
</details>

<details id="AssessmentRepositoryImpl">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/assessment/AssessmentRepositoryImpl.java">AssessmentRepositoryImpl.java</a> [@Repository]</strong></summary>
<blockquote>

**implements** [`AssessmentRepository`](#AssessmentRepository) · **dependencias** [`AssessmentJpaRepository`](#AssessmentJpaRepository)

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

**tipo** `record` · **campos** `int status`, `String error`, `String message`, `String timestamp`

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
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/auth/AuthController.java">AuthController.java</a> [@RestController]</strong></summary>
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

<details id="UserController">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/user/UserController.java">UserController.java</a> [@RestController]</strong></summary>
<blockquote>

**dependencias** [`UserService`](#UserService)

| Método | Path | Auth | Status |
|---|---|---|---|
| POST | `/api/v1/users` | público | 201 |
| POST | `/api/v1/users/admin` | ADMIN | 201 |
| GET | `/api/v1/users` | ADMIN | 200 |
| GET | `/api/v1/users/{email}` | público | 200 |
| PATCH | `/api/v1/users/{id}/name` | autenticado | 200 |
| PATCH | `/api/v1/users/{id}/email` | autenticado | 200 |
| DELETE | `/api/v1/users/{id}` | autenticado | 204 |

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-presentation-course">
<summary><strong>presentation/course/</strong></summary>
<blockquote>

<details id="CourseController">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/course/CourseController.java">CourseController.java</a> [@RestController]</strong></summary>
<blockquote>

**dependencias** [`CourseService`](#CourseService)

| Método | Path | Status |
|---|---|---|
| POST | `/api/v1/users/{userId}/courses` | 201 |
| GET | `/api/v1/users/{userId}/courses` | 200 |
| PATCH | `/api/v1/users/{userId}/courses/{id}/name` | 200 |
| PATCH | `/api/v1/users/{userId}/courses/{id}/average-method` | 200 |
| PATCH | `/api/v1/users/{userId}/courses/{id}/cut-off-grade` | 200 |
| DELETE | `/api/v1/users/{userId}/courses/{id}` | 204 |

</blockquote>
</details>

<details id="dir-presentation-course-dto">
<summary><strong>presentation/course/dto/</strong></summary>
<blockquote>

- [CreateCourseRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/course/dto/CreateCourseRequest.java) — request de criação com validações
- [UpdateCourseNameRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/course/dto/UpdateCourseNameRequest.java)
- [UpdateAverageMethodRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/course/dto/UpdateAverageMethodRequest.java)
- [UpdateCutOffGradeRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/course/dto/UpdateCutOffGradeRequest.java)

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-presentation-projection">
<summary><strong>presentation/projection/</strong></summary>
<blockquote>

<details id="ProjectionController">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/projection/ProjectionController.java">ProjectionController.java</a> [@RestController]</strong></summary>
<blockquote>

**dependencias** [`ProjectionService`](#ProjectionService) · [`CourseService`](#CourseService)

| Método | Path | Status |
|---|---|---|
| POST | `/api/v1/users/{userId}/courses/{courseId}/projections` | 201 |
| GET | `/api/v1/users/{userId}/courses/{courseId}/projections` | 200 |
| GET | `/api/v1/users/{userId}/projections` | 200 |
| PATCH | `/api/v1/users/{userId}/courses/{courseId}/projections/{id}` | 200 |
| DELETE | `/api/v1/users/{userId}/courses/{courseId}/projections/{id}` | 204 |

</blockquote>
</details>

</blockquote>
</details>

<details id="dir-presentation-assessment">
<summary><strong>presentation/assessment/</strong></summary>
<blockquote>

<details id="AssessmentController">
<summary><strong><a href="src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/assessment/AssessmentController.java">AssessmentController.java</a> [@RestController]</strong></summary>
<blockquote>

**dependencias** [`AssessmentService`](#AssessmentService) · [`CourseService`](#CourseService)

| Método | Path | Status |
|---|---|---|
| GET | `/api/v1/users/{userId}/courses/{courseId}/projections/{projectionId}/assessments` | 200 |
| GET | `/api/v1/users/{userId}/courses/{courseId}/projections/{projectionId}/assessments/{id}` | 200 |
| PATCH | `/api/v1/users/{userId}/courses/{courseId}/projections/{projectionId}/assessments/{id}/grade` | 200 |

</blockquote>
</details>

<details id="dir-presentation-assessment-dto">
<summary><strong>presentation/assessment/dto/</strong></summary>
<blockquote>

- [ApplyGradeRequest.java](src/main/java/br/com/gustavohenrique/cleanmediasapi/presentation/assessment/dto/ApplyGradeRequest.java) — record: `@NotNull Double grade`

</blockquote>
</details>

</blockquote>
</details>

</blockquote>
</details>

---

## src/main/resources

- [application.properties](src/main/resources/application.properties) — configuração principal (datasource, JPA, Flyway, JWT, CORS, Swagger)
- [db/migration/V1__create_schema.sql](src/main/resources/db/migration/V1__create_schema.sql) — schema completo: `users`, `courses`, `projections`, `assessments` com FK cascades

---

## src/test

| Classe de Teste | Tipo | Cobertura |
|---|---|---|
| [CleanMediasApiApplicationTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/CleanMediasApiApplicationTest.java) | @SpringBootTest + H2 | smoke: contexto sobe sem erros |
| [UserTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/domain/user/UserTest.java) | JUnit puro | construtores e getters de `User` |
| [AssessmentTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/domain/assessment/AssessmentTest.java) | JUnit puro | invariante `applyGrade()`, limites, negativo |
| [FormulaParserTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/domain/formula/FormulaParserTest.java) | JUnit puro | extração de identificadores e maxValue, tokenização |
| [ShuntingYardTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/domain/formula/ShuntingYardTest.java) | JUnit puro | precedência, parênteses, anotação `@M[n]:argCount` |
| [RpnEvaluatorTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/domain/formula/RpnEvaluatorTest.java) | JUnit puro | média simples, ponderada, top-n, fórmula complexa |
| [AuthServiceImplTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/application/auth/AuthServiceImplTest.java) | Mockito | fluxo de autenticação (sucesso e falhas) |
| [UserServiceImplTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/application/user/UserServiceImplTest.java) | Mockito | CRUD de usuários, email único, 12 casos |
| [CourseServiceImplTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/application/course/CourseServiceImplTest.java) | Mockito | CRUD de cursos, cascata de projeções, 10 casos |
| [ProjectionServiceImplTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/application/projection/ProjectionServiceImplTest.java) | Mockito | CRUD de projeções, inicialização de assessments, 11 casos |
| [AssessmentServiceImplTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/application/assessment/AssessmentServiceImplTest.java) | Mockito | initializeForProjection, applyGrade com RPN, 8 casos |
| [JwtServiceTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/security/JwtServiceTest.java) | JUnit puro | geração e decodificação de JWT RSA |
| [UserRepositoryImplTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/user/UserRepositoryImplTest.java) | @DataJpaTest + H2 | CRUD e queries do repositório de usuários |
| [CourseRepositoryImplTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/course/CourseRepositoryImplTest.java) | @DataJpaTest + H2 | CRUD e queries do repositório de cursos |
| [ProjectionRepositoryImplTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/projection/ProjectionRepositoryImplTest.java) | @DataJpaTest + H2 | CRUD e queries do repositório de projeções |
| [AssessmentRepositoryImplTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/infrastructure/persistence/assessment/AssessmentRepositoryImplTest.java) | @DataJpaTest + H2 | CRUD e queries do repositório de assessments |
| [GlobalExceptionHandlerTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/presentation/exception/GlobalExceptionHandlerTest.java) | @WebMvcTest | mapeamento de exceções → status HTTP |
| [AuthControllerTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/presentation/auth/AuthControllerTest.java) | @WebMvcTest | endpoints de autenticação |
| [UserControllerTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/presentation/user/UserControllerTest.java) | @WebMvcTest | 12 casos do UserController |
| [CourseControllerTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/presentation/course/CourseControllerTest.java) | @WebMvcTest | 10 casos do CourseController |
| [ProjectionControllerTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/presentation/projection/ProjectionControllerTest.java) | @WebMvcTest | 9 casos do ProjectionController |
| [AssessmentControllerTest](src/test/java/br/com/gustavohenrique/cleanmediasapi/presentation/assessment/AssessmentControllerTest.java) | @WebMvcTest | 6 casos do AssessmentController |
