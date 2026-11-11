# Sistema de Biblioteca Escolar — Spring Boot + JPA

Aplicação REST que gerencia **leitores**, **carteirinhas**, **livros**, **categorias** e **empréstimos**,
implementando os três relacionamentos JPA exigidos na atividade:

| Desafio | Relacionamento | Entidades | FK / tabela |
|---|---|---|---|
| 1 | `@OneToOne` | `Leitor` ↔ `Carteirinha` | `carteirinha.leitor_id` (UNIQUE) |
| 2 | `@OneToMany` / `@ManyToOne` | `Leitor` → `Emprestimo` | `emprestimo.leitor_id` (NOT NULL) |
| 3 | `@ManyToMany` | `Livro` ↔ `Categoria` | tabela `livro_categoria` |

## Tecnologias

- Java 17
- Spring Boot 3.3.5 (Web, Data JPA, Validation)
- H2 (banco em memória, com console web em `/h2-console`)
- Maven
- JUnit 5 + AssertJ (testes de integração)

## Como executar

O projeto já traz o **Maven Wrapper**, então não é preciso ter o Maven instalado:

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

ou, com o Maven instalado:

```bash
mvn spring-boot:run
```

ou gerando o jar executável:

```bash
mvnw.cmd clean package
java -jar target/biblioteca-1.0.0.jar
```

A aplicação sobe em `http://localhost:8080` e já popula o banco com dados de exemplo
(`DataSeeder`). Para desligar a carga automática: `app.seed.enabled=false`.

Console do H2: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:biblioteca`
- User: `sa` | Password: *(vazio)*

## Testes

```bash
mvnw.cmd test
```

Os testes em `RelacionamentosIntegrationTest` verificam a persistência real dos três
relacionamentos (inclusive a criação das constraints `UNIQUE` e da tabela intermediária).

## Estrutura do projeto

```
src/main/java/com/escola/biblioteca
├── BibliotecaApplication.java      # classe principal (@SpringBootApplication)
├── config/DataSeeder.java           # dados de exemplo (CommandLineRunner)
├── controller/                      # camada REST
│   ├── LeitorController.java
│   ├── CarteirinhaController.java
│   ├── EmprestimoController.java
│   ├── LivroController.java
│   └── CategoriaController.java
├── dto/                             # records de entrada/saída + Bean Validation
├── entity/                          # entidades JPA (modelo relacional)
│   ├── Leitor.java
│   ├── Carteirinha.java
│   ├── Emprestimo.java
│   ├── StatusEmprestimo.java
│   ├── Livro.java
│   └── Categoria.java
├── exception/                       # tratamento global de erros (404/422/400)
├── repository/                      # Spring Data JPA
└── service/                         # regras de negócio (@Transactional)
```

## Endpoints

### Leitores e carteirinhas (Desafio 1)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/leitores` | Cadastra leitor **com** sua carteirinha |
| GET | `/api/leitores` | Lista todos os leitores |
| GET | `/api/leitores/{id}` | Detalha um leitor |
| PUT | `/api/leitores/{id}` | Atualiza leitor (e dados da carteirinha) |
| GET | `/api/leitores/{id}/emprestimos` | Lista os empréstimos do leitor |
| POST | `/api/carteirinhas/leitor/{leitorId}` | Emite carteirinha para leitor sem carteirinha |
| GET | `/api/carteirinhas/leitor/{leitorId}` | Consulta a carteirinha do leitor |

```bash
curl -X POST http://localhost:8080/api/leitores \
  -H "Content-Type: application/json" \
  -d '{
        "nome": "Maria Oliveira",
        "email": "maria.oliveira@escola.edu.br",
        "telefone": "11977778888",
        "dataNascimento": "2009-03-14",
        "carteirinha": {
          "numero": "CAR-2026-0100",
          "dataEmissao": "2026-02-01",
          "dataValidade": "2027-02-01",
          "tipo": "ESTUDANTE"
        }
      }'
```

### Empréstimos (Desafio 2)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/emprestimos` | Registra empréstimo (leitor + livro) |
| GET | `/api/emprestimos` | Lista todos os empréstimos |
| GET | `/api/emprestimos/leitor/{leitorId}` | Empréstimos de um leitor |
| POST | `/api/emprestimos/{id}/devolucao` | Registra devolução |

```bash
curl -X POST http://localhost:8080/api/emprestimos \
  -H "Content-Type: application/json" \
  -d '{"leitorId": 1, "livroId": 1, "dataDevolucaoPrevista": "2026-03-01"}'
```

Regras aplicadas no serviço: leitor precisa estar **ativo** e **possuir carteirinha**;
limite de **3 empréstimos ativos** por leitor.

### Livros e categorias (Desafio 3)

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/categorias` | Cadastra categoria |
| GET | `/api/categorias` | Lista categorias |
| POST | `/api/livros` | Cadastra livro já com categorias |
| GET | `/api/livros` | Lista livros |
| GET | `/api/livros/categoria/{categoriaId}` | Livros de uma categoria |
| PUT | `/api/livros/{id}/categorias` | Substitui as categorias do livro |

```bash
curl -X POST http://localhost:8080/api/livros \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Spring Boot na Prática", "isbn": "978-8550000000",
       "anoPublicacao": 2024, "quantidadeExemplares": 5,
       "categoriaIds": [1, 2]}'
```

## Modelo relacional gerado pelo JPA

```sql
CREATE TABLE leitor (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(120) NOT NULL,
    email           VARCHAR(160) NOT NULL UNIQUE,
    telefone        VARCHAR(20),
    data_nascimento DATE,
    ativo           BOOLEAN NOT NULL
);

CREATE TABLE carteirinha (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero         VARCHAR(20) NOT NULL UNIQUE,
    data_emissao   DATE NOT NULL,
    data_validade  DATE NOT NULL,
    tipo           VARCHAR(30),
    leitor_id      BIGINT UNIQUE,          -- FK: um leitor, no máximo, uma carteirinha
    CONSTRAINT fk_carteirinha_leitor FOREIGN KEY (leitor_id) REFERENCES leitor (id)
);

CREATE TABLE emprestimo (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_emprestimo         DATE NOT NULL,
    data_devolucao_prevista DATE,
    data_devolucao          DATE,
    status                  VARCHAR(20) NOT NULL,   -- H2/MySQL: ENUM; Postgres: VARCHAR + CHECK
    leitor_id               BIGINT NOT NULL,  -- FK: o ManyToMany não existe aqui, é 1:N
    livro_id                BIGINT NOT NULL,
    CONSTRAINT fk_emprestimo_leitor FOREIGN KEY (leitor_id) REFERENCES leitor (id),
    CONSTRAINT fk_emprestimo_livro  FOREIGN KEY (livro_id)  REFERENCES livro (id)
);

CREATE TABLE livro (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo                 VARCHAR(160) NOT NULL,
    isbn                   VARCHAR(20) UNIQUE,
    ano_publicacao         INTEGER,
    quantidade_exemplares  INTEGER NOT NULL
);

CREATE TABLE categoria (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(80) NOT NULL UNIQUE,
    descricao   VARCHAR(255)
);

-- tabela intermediária do ManyToMany
CREATE TABLE livro_categoria (
    livro_id     BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (livro_id, categoria_id),
    CONSTRAINT fk_lc_livro     FOREIGN KEY (livro_id)     REFERENCES livro (id),
    CONSTRAINT fk_lc_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id)
);
```

O script SQL equivalente está em [`docs/schema.sql`](docs/schema.sql).

### Como cada anotação foi usada

**Desafio 1 — `@OneToOne`**

```java
// Lado inverso (mappedBy) — em Leitor
@OneToOne(mappedBy = "leitor", cascade = CascadeType.ALL, orphanRemoval = true)
private Carteirinha carteirinha;

// Lado dono (FK) — em Carteirinha
@OneToOne
@JoinColumn(name = "leitor_id", unique = true)
private Leitor leitor;
```

`unique = true` no `@JoinColumn` é o que cria a restrição de unicidade no banco: sem ela,
duas carteirinhas poderiam apontar para o mesmo leitor.

**Desafio 2 — `@OneToMany` / `@ManyToOne`**

```java
// em Leitor
@OneToMany(mappedBy = "leitor", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Emprestimo> emprestimos = new ArrayList<>();

// em Emprestimo (entidade responsável pela FK)
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "leitor_id", nullable = false)
private Leitor leitor;
```

`optional = false` + `nullable = false` garantem, no banco, que todo empréstimo pertence
a exatamente um leitor.

**Desafio 3 — `@ManyToMany` + `@JoinTable`**

```java
// em Livro (dono da relação)
@ManyToMany
@JoinTable(name = "livro_categoria",
           joinColumns = @JoinColumn(name = "livro_id"),
           inverseJoinColumns = @JoinColumn(name = "categoria_id"))
private Set<Categoria> categorias = new LinkedHashSet<>();

// em Categoria (lado inverso)
@ManyToMany(mappedBy = "categorias")
private Set<Livro> livros = new LinkedHashSet<>();
```

A chave primária composta `(livro_id, categoria_id)` impede a duplicidade do mesmo par
livro/categoria.

## Respostas às questões norteadoras

**1. Como garantir que cada leitor possua apenas uma carteirinha e que cada carteirinha pertença
exclusivamente a um leitor?**

O vínculo é feito pela coluna `carteirinha.leitor_id`, criada pelo `@JoinColumn(name = "leitor_id",
unique = true)` do lado dono (`Carteirinha`). A restrição `UNIQUE` garante no banco que um mesmo
leitor não pode estar referenciado por duas carteirinhas, e a FK garante que a carteirinha sempre
aponte para um leitor existente. No código, o `LeitorService.cadastrar` preenche os dois lados do
relacionamento (`carteirinha.setLeitor(leitor)` e `leitor.setCarteirinha(carteirinha)`) e o
`CarteirinhaService.emitir` recusa emitir uma segunda carteirinha para o mesmo leitor
(`RegraDeNegocioException`). O `cascade = CascadeType.ALL` com `orphanRemoval = true` faz a
carteirinha ser gravada junto com o leitor e removida caso seja desvinculada.

**2. Como representar no banco de dados um leitor que possui vários empréstimos registrados?**

Com a FK `emprestimo.leitor_id`: a tabela `emprestimo` possui uma coluna `leitor_id` que referencia
`leitor(id)`, e **não** uma coluna na tabela `leitor` apontando para vários empréstimos. Assim, N
linhas de `emprestimo` podem apontar para a mesma linha de `leitor` (1:N). No modelo Java,
`Leitor.emprestimos` é a coleção (`@OneToMany(mappedBy = "leitor")`) e `Emprestimo.leitor` é a
referência (`@ManyToOne`). As consultas são feitas por
`EmprestimoRepository.findByLeitorIdOrderByDataEmprestimoDesc(leitorId)` e expostas em
`GET /api/leitores/{id}/emprestimos` e `GET /api/emprestimos/leitor/{leitorId}`.

**3. Como organizar os livros em várias categorias?**

Com a tabela intermediária `livro_categoria`, gerada por `@JoinTable`. Cada linha da tabela
intermediária representa um par (livro, categoria), permitindo que um livro pertença a várias
categorias e que uma categoria tenha vários livros. A PK composta `(livro_id, categoria_id)` evita
vínculos duplicados. Em Java, `Livro.categorias` (`@ManyToMany` com `@JoinTable`) é o lado dono e
`Categoria.livros` (`@ManyToMany(mappedBy = "categorias")`) é o lado inverso. Ao cadastrar um livro,
o serviço resolve as categorias por id (`LivroService.resolverCategorias`) e o Hibernate grava as
linhas na tabela intermediária.

## Boas práticas aplicadas

- **DTOs (`record`)** isolam a API das entidades JPA — o JSON de entrada nunca é a entidade.
- **Bean Validation** (`@NotBlank`, `@NotNull`, `@Email`) valida os dados na entrada.
- **Camadas separadas**: `controller` → `service` → `repository`; regras de negócio e
  `@Transactional` ficam no serviço.
- **`open-in-view=false`** evita manter a sessão do Hibernate aberta durante a serialização
  (evita o problema de `LazyInitializationException`).
- **Tratamento global de erros** (`GlobalExceptionHandler`): 404 para recurso inexistente,
  422 para regra de negócio e 400 para payload inválido.
- **Nomes explícitos de coluna e tabela** (`@Table`, `@Column`) para o banco ficar legível.
- **Testes de integração** validando a persistência real dos três relacionamentos.

## Requisitos / observações

- O projeto usa **Maven** (com Maven Wrapper incluído); o `pom.xml` já declara o
  `spring-boot-starter-parent` 3.3.5. Se preferir o Maven do IntelliJ, use a barra lateral
  **Maven** → *Download Maven*.
- Para usar MySQL/PostgreSQL em produção, troque as properties de `spring.datasource.*` e use
  `spring.jpa.hibernate.ddl-auto=validate` (ou `update` em ambiente de teste).
