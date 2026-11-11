package com.escola.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record LivroRequest(
        @NotBlank(message = "O título é obrigatório")
        String titulo,

        String isbn,

        Integer anoPublicacao,

        @PositiveOrZero(message = "A quantidade de exemplares não pode ser negativa")
        Integer quantidadeExemplares,

        List<Long> categoriaIds
) {
}
