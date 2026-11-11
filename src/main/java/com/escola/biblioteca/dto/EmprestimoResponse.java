package com.escola.biblioteca.dto;

import com.escola.biblioteca.entity.Emprestimo;
import com.escola.biblioteca.entity.StatusEmprestimo;

import java.time.LocalDate;

public record EmprestimoResponse(
        Long id,
        LocalDate dataEmprestimo,
        LocalDate dataDevolucaoPrevista,
        LocalDate dataDevolucao,
        StatusEmprestimo status,
        Long leitorId,
        String leitorNome,
        Long livroId,
        String livroTitulo
) {
    public static EmprestimoResponse from(Emprestimo emprestimo) {
        return new EmprestimoResponse(
                emprestimo.getId(),
                emprestimo.getDataEmprestimo(),
                emprestimo.getDataDevolucaoPrevista(),
                emprestimo.getDataDevolucao(),
                emprestimo.getStatus(),
                emprestimo.getLeitor() != null ? emprestimo.getLeitor().getId() : null,
                emprestimo.getLeitor() != null ? emprestimo.getLeitor().getNome() : null,
                emprestimo.getLivro() != null ? emprestimo.getLivro().getId() : null,
                emprestimo.getLivro() != null ? emprestimo.getLivro().getTitulo() : null
        );
    }
}
