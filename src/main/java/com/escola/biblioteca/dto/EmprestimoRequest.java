package com.escola.biblioteca.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EmprestimoRequest(
        @NotNull(message = "O leitor é obrigatório")
        Long leitorId,

        @NotNull(message = "O livro é obrigatório")
        Long livroId,

        LocalDate dataEmprestimo,

        LocalDate dataDevolucaoPrevista
) {
}
