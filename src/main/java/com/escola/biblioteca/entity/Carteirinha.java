package com.escola.biblioteca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * DESAFIO 1 - Entidade "dona" da chave estrangeira do relacionamento OneToOne.
 * A coluna "leitor_id" é UNIQUE, o que garante no banco que uma carteirinha
 * pertence a no máximo um leitor (e um leitor tem no máximo uma carteirinha).
 */
@Entity
@Table(name = "carteirinha")
public class Carteirinha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String numero;

    @Column(nullable = false)
    private LocalDate dataEmissao;

    @Column(nullable = false)
    private LocalDate dataValidade;

    @Column(length = 30)
    private String tipo;

    /**
     * Lado dono do OneToOne: a FK "leitor_id" é criada aqui.
     * unique = true no JoinColumn cria a restrição UNIQUE no banco.
     */
    @OneToOne
    @JoinColumn(name = "leitor_id", unique = true)
    private Leitor leitor;

    public Carteirinha() {
    }

    public Carteirinha(String numero, LocalDate dataEmissao, LocalDate dataValidade, String tipo) {
        this.numero = numero;
        this.dataEmissao = dataEmissao;
        this.dataValidade = dataValidade;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Leitor getLeitor() {
        return leitor;
    }

    public void setLeitor(Leitor leitor) {
        this.leitor = leitor;
    }
}
