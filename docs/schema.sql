-- ============================================================================
-- Modelo relacional da biblioteca escolar (gerado pelo JPA/Hibernate).
-- Este script é a referência do schema; a aplicação cria as tabelas
-- automaticamente via spring.jpa.hibernate.ddl-auto=update.
-- ============================================================================

-- DESAFIO 1 - Leitor x Carteirinha (OneToOne)
CREATE TABLE leitor (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(120) NOT NULL,
    email           VARCHAR(160) NOT NULL UNIQUE,
    telefone        VARCHAR(20),
    data_nascimento DATE,
    ativo           BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE carteirinha (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero         VARCHAR(20) NOT NULL UNIQUE,
    data_emissao   DATE NOT NULL,
    data_validade  DATE NOT NULL,
    tipo           VARCHAR(30),
    leitor_id      BIGINT UNIQUE,
    CONSTRAINT fk_carteirinha_leitor FOREIGN KEY (leitor_id) REFERENCES leitor (id)
);

-- DESAFIO 2 - Leitor x Emprestimo (OneToMany / ManyToOne)
CREATE TABLE livro (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo                VARCHAR(160) NOT NULL,
    isbn                  VARCHAR(20) UNIQUE,
    ano_publicacao        INTEGER,
    quantidade_exemplares INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE emprestimo (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_emprestimo         DATE NOT NULL,
    data_devolucao_prevista DATE,
    data_devolucao          DATE,
    status                  VARCHAR(20) NOT NULL,
    leitor_id               BIGINT NOT NULL,
    livro_id                BIGINT NOT NULL,
    CONSTRAINT fk_emprestimo_leitor FOREIGN KEY (leitor_id) REFERENCES leitor (id),
    CONSTRAINT fk_emprestimo_livro  FOREIGN KEY (livro_id)  REFERENCES livro (id)
);

-- DESAFIO 3 - Livro x Categoria (ManyToMany)
CREATE TABLE categoria (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(80) NOT NULL UNIQUE,
    descricao   VARCHAR(255)
);

CREATE TABLE livro_categoria (
    livro_id     BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (livro_id, categoria_id),
    CONSTRAINT fk_lc_livro     FOREIGN KEY (livro_id)     REFERENCES livro (id),
    CONSTRAINT fk_lc_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id)
);

-- Consultas úteis para demonstrar os relacionamentos
-- 1) Emprestimos de um leitor (1:N)
-- SELECT e.*, l.titulo FROM emprestimo e JOIN livro l ON l.id = e.livro_id
--  WHERE e.leitor_id = 1 ORDER BY e.data_emprestimo DESC;

-- 2) Livros de uma categoria (N:M)
-- SELECT l.* FROM livro l JOIN livro_categoria lc ON lc.livro_id = l.id
--  JOIN categoria c ON c.id = lc.categoria_id WHERE c.nome = 'Programação';

-- 3) Leitor e sua carteirinha (1:1)
-- SELECT le.nome, c.numero FROM leitor le JOIN carteirinha c ON c.leitor_id = le.id;
