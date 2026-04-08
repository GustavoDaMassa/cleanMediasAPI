# cleanMediasAPI

Reimplementação da [MediasAPI](https://github.com/GustavoDaMassa/MediasAPI) com **Clean Architecture** — mesma lógica de negócio, estrutura completamente diferente.

O objetivo do projeto é demonstrar na prática como a arquitetura limpa isola o domínio de qualquer detalhe técnico, tornando o código mais testável, substituível e legível.

---

## Stack

| Componente | Tecnologia |
|---|---|
| Runtime | Java 21 |
| Framework | Spring Boot 3.5.0 |
| Banco de dados | MySQL 8 |
| ORM | Spring Data JPA / Hibernate |
| Migrations | Flyway |
| Auth | JWT com RSA (Spring OAuth2 Resource Server) |
| Mapeamento | ModelMapper |
| Boilerplate | Lombok |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven |

---

## Modelo de dados

```
User (1)
 └── Course (N)
      └── Projection (N)
           └── Assessment (N)
```

- **User** — conta de acesso
- **Course** — disciplina com fórmula de média (`averageMethod`) e nota de corte (`cutOffGrade`)
- **Projection** — cenário de nota final calculado pela fórmula
- **Assessment** — avaliação individual (`AV1`, `AT1`, etc.) com valor máximo e nota aplicada

---

## O que é Clean Architecture

A Clean Architecture é um estilo arquitetural cujo princípio central é: **o domínio não depende de nada**. Framework, banco de dados, segurança e HTTP são detalhes de infraestrutura — o domínio dita as regras, o resto se adapta a ele.

As dependências apontam sempre para dentro:

```
┌──────────────────────────────────────┐
│           presentation               │  HTTP, controllers, DTOs de entrada/saída
├──────────────────────────────────────┤
│           application                │  Serviços, orquestração de casos de uso
├──────────────────────────────────────┤
│             domain                   │  Entidades puras, interfaces, regras de negócio
├──────────────────────────────────────┤
│          infrastructure              │  JPA, segurança, configs — detalhes técnicos
└──────────────────────────────────────┘
```

`infrastructure` e `presentation` conhecem o `domain`. O `domain` não conhece nenhum deles.

---

## Estrutura de pacotes

```
br.com.gustavohenrique.cleanmediasapi/
│
├── domain/                        ← núcleo — Java puro, zero dependência de framework
│   ├── user/
│   │   ├── User.java              ← entidade pura (sem @Entity, sem Lombok)
│   │   ├── UserRepository.java    ← interface — contrato definido pelo domínio
│   │   ├── UserNotFoundException.java
│   │   └── Role.java
│   ├── course/
│   ├── projection/
│   ├── assessment/
│   ├── formula/                   ← pipeline de cálculo RPN
│   │   ├── FormulaParser.java
│   │   ├── ShuntingYard.java
│   │   ├── RpnEvaluator.java
│   │   └── FormulaToken.java
│   └── exception/
│       ├── NotFoundException.java  ← abstract → HTTP 404
│       └── BusinessException.java  ← HTTP 400
│
├── application/                   ← casos de uso — depende apenas do domain
│   ├── user/
│   │   ├── UserService.java        ← interface do serviço
│   │   ├── UserServiceImpl.java
│   │   └── dto/
│   ├── course/
│   ├── projection/
│   ├── assessment/
│   └── auth/
│       ├── AuthService.java
│       ├── AuthServiceImpl.java
│       ├── TokenGenerator.java     ← interface — o domínio define o contrato de geração de token
│       └── dto/
│
├── infrastructure/                ← detalhes técnicos — implementa contratos do domain
│   ├── persistence/
│   │   ├── user/
│   │   │   ├── UserJpaEntity.java          ← @Entity fica aqui, não no domain
│   │   │   ├── UserJpaRepository.java      ← extends JpaRepository — detalhe de infra
│   │   │   └── UserRepositoryImpl.java     ← implementa UserRepository do domain
│   │   ├── course/
│   │   ├── projection/
│   │   └── assessment/
│   ├── security/
│   │   ├── JwtService.java                 ← implementa TokenGenerator
│   │   ├── JwtTokenGenerator.java
│   │   ├── JwtAuthFilter.java
│   │   ├── UserDetailsServiceImpl.java     ← ponte entre User (domain) e Spring Security
│   │   └── SecurityConfig.java
│   └── config/
│       ├── ModelMapperConfig.java
│       ├── SwaggerConfig.java
│       └── CorsConfig.java
│
└── presentation/                  ← HTTP — controllers REST e tratamento de erros
    ├── user/UserController.java
    ├── course/CourseController.java
    ├── projection/ProjectionController.java
    ├── assessment/AssessmentController.java
    ├── auth/AuthController.java
    └── exception/
        ├── GlobalExceptionHandler.java
        └── ErrorResponse.java
```

---

## Como cada camada funciona na prática

### domain — sem framework

A entidade `Assessment` é Java puro. A regra de que uma nota não pode exceder o valor máximo é protegida pela própria estrutura da classe — o campo `grade` não tem setter:

```java
public class Assessment {            // sem @Entity, sem Lombok, sem Spring

    private double grade;
    private boolean fixed;

    public void applyGrade(double grade) {
        if (grade < 0) throw new BusinessException("Grade cannot be negative");
        if (grade > maxValue) throw new BusinessException("Grade " + grade + " exceeds max value " + maxValue);
        this.grade = grade;
        this.fixed = true;           // marca como fixo — não pode ser alterado depois
    }
}
```

O `AssessmentRepository` no domínio é uma interface Java pura — define o contrato sem saber como será implementado:

```java
public interface AssessmentRepository {
    Assessment save(Assessment assessment);
    Optional<Assessment> findByIdAndProjectionId(Long id, Long projectionId);
    List<Assessment> findAllByProjectionId(Long projectionId);
}
```

### infrastructure — implementa o contrato

Cada entidade tem dois objetos separados: a entidade de domínio pura e a entidade JPA:

```java
@Entity
@Table(name = "assessments")
public class AssessmentJpaEntity {

    @Column(name = "projection_id")
    private Long projectionId;      // só o ID — sem @ManyToOne, sem navegação JPA

    public static AssessmentJpaEntity fromDomain(Assessment a) { ... }
    public Assessment toDomain() { ... }
}
```

O `AssessmentRepositoryImpl` implementa a interface do domínio usando JPA internamente:

```java
@Repository
public class AssessmentRepositoryImpl implements AssessmentRepository {

    public Assessment save(Assessment assessment) {
        return jpa.save(AssessmentJpaEntity.fromDomain(assessment)).toDomain();
    }
}
```

O domínio passa um `Assessment` puro. A infraestrutura converte, persiste e devolve. O domínio nunca soube que o Hibernate existe.

### application — orquestração

Os serviços falam apenas em termos de domínio e recebem o `userId` como parâmetro — não acessam o `SecurityContextHolder` do Spring Security:

```java
public AssessmentResponse applyGrade(Long projectionId, Long assessmentId, double grade, String averageMethod) {
    Assessment assessment = assessmentRepository.findByIdAndProjectionId(assessmentId, projectionId)
            .orElseThrow(() -> new AssessmentNotFoundException(assessmentId, projectionId));

    assessment.applyGrade(grade);   // regra de negócio no domínio
    assessmentRepository.save(assessment);
    recalculateFinalGrade(projectionId, averageMethod);
}
```

### presentation — HTTP como detalhe

Os controllers extraem o `userId` do path e delegam ao serviço. É o único lugar que conhece HTTP e Spring Security:

```java
@GetMapping
public ResponseEntity<List<CourseResponse>> listAll(@PathVariable Long userId) {
    return ResponseEntity.ok(courseService.listAll(userId));
}
```

---

## Pipeline de cálculo de notas

A fórmula de média é uma expressão matemática configurável por disciplina. O pipeline transforma a string em resultado numérico:

```
"(AV1 + AV2) / 2"
       │
       ▼
FormulaParser        → tokeniza a string e extrai identificadores com seus maxValues
       │
       ▼
ShuntingYard         → converte para Notação Polonesa Reversa (RPN)
       │
       ▼
RpnEvaluator         → avalia o RPN com as notas atuais
       │
       ▼
finalGrade (double)
```

Todo o pipeline vive no `domain/formula/` — lógica pura sem nenhuma dependência de framework.

---

## Estratégia de testes

Cada camada é testada no seu nível de isolamento correto:

| Camada | Ferramenta | Anotação |
|---|---|---|
| domain | JUnit 5 puro | nenhuma — instancia e testa |
| infrastructure | Spring Data JPA + H2 | `@DataJpaTest` |
| application | Mockito | `@ExtendWith(MockitoExtension.class)` |
| presentation | MockMvc | `@WebMvcTest` + `@MockitoBean` |

Exemplo de teste de domínio — sem Spring, sem banco, sem mock:

```java
@Test
void shouldThrowWhenGradeExceedsMaxValue() {
    Assessment a = new Assessment(1L, "AV1", 10.0);
    assertThatThrownBy(() -> a.applyGrade(11.0))
            .isInstanceOf(BusinessException.class)
            .hasMessage("Grade 10.1 exceeds max value 10.0 for AV1");
}
```

---

## Como rodar localmente

```bash
# Subir banco de dados
docker compose up -d mysql

# Rodar a aplicação
./mvnw spring-boot:run

# Rodar os testes
./mvnw test
```

Swagger UI disponível em: `http://localhost:8080/swagger-ui/index.html`

### Variáveis de ambiente

Copie `.env.example` para `.env` e preencha:

```env
DATABASE_URL=jdbc:mysql://localhost:3306/cleanmediasapi
DATABASE_USERNAME=cleanmediasapi
DATABASE_PASSWORD=sua-senha

# Gerar par RSA: openssl genrsa -out private.pem 2048
#                openssl rsa -in private.pem -pubout -out public.pem
JWT_PRIVATE_KEY_CONTENT=<base64 do private.pem>
JWT_PUBLIC_KEY_CONTENT=<base64 do public.pem>

CORS_ALLOWED_ORIGINS=http://localhost:3000
```

---

## Deploy

Ver [DEPLOY.md](DEPLOY.md) — guia completo para o home server com Docker, Cloudflare Tunnel e deploy automático via Watchtower + GitHub Actions.

---

## Comparação com a versão anterior

| | MediasAPI (camadas) | cleanMediasAPI (clean arch) |
|---|---|---|
| Entidade de domínio | `@Entity` + `@ManyToOne` | Java puro, sem anotação |
| Repositório | estende `JpaRepository` | interface no domain, impl na infra |
| Autenticação no serviço | `SecurityContextHolder` | `userId` como parâmetro |
| Teste de regra de negócio | precisa de contexto JPA | JUnit puro |
| Trocar banco de dados | mexe na entidade | só `infrastructure/persistence/` |
