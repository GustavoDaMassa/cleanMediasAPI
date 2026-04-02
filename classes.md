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

<details id="dir-domain-user">
<summary><strong>domain/user/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

<details id="dir-domain-course">
<summary><strong>domain/course/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

<details id="dir-domain-projection">
<summary><strong>domain/projection/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

<details id="dir-domain-assessment">
<summary><strong>domain/assessment/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

<details id="dir-domain-formula">
<summary><strong>domain/formula/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

---

### application/

<details id="dir-application-user">
<summary><strong>application/user/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

<details id="dir-application-course">
<summary><strong>application/course/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

<details id="dir-application-projection">
<summary><strong>application/projection/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

<details id="dir-application-assessment">
<summary><strong>application/assessment/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

---

### infrastructure/

<details id="dir-infra-persistence">
<summary><strong>infrastructure/persistence/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

<details id="dir-infra-security">
<summary><strong>infrastructure/security/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 4_

</blockquote>
</details>

---

### presentation/

<details id="dir-presentation-exception">
<summary><strong>presentation/exception/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 4_

</blockquote>
</details>

<details id="dir-presentation-user">
<summary><strong>presentation/user/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

<details id="dir-presentation-course">
<summary><strong>presentation/course/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

<details id="dir-presentation-projection">
<summary><strong>presentation/projection/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

<details id="dir-presentation-assessment">
<summary><strong>presentation/assessment/</strong></summary>
<blockquote>

_vazio — a preencher na Fase 5_

</blockquote>
</details>

</blockquote>
</details>

---

## src/main/resources

- [application.properties](src/main/resources/application.properties) — configuração principal
- [db/migration/](src/main/resources/db/migration/) — scripts Flyway (criados na Fase 6)

---

## src/test

_testes a preencher por fase_
