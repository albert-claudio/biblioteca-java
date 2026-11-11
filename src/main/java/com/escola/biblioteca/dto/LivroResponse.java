package com.escola.biblioteca.dto;

import com.escola.biblioteca.entity.Livro;

import java.util.Set;
import java.util.stream.Collectors;

public record LivroResponse(
        Long id,
        String titulo,
        String isbn,
        Integer anoPublicacao,
        Integer quantidadeExemplares,
        Set<String> categorias
) {
    public static LivroResponse from(Livro livro) {
        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getAnoPublicacao(),
                livro.getQuantidadeExemplares(),
                livro.getCategorias() != null
                        ? livro.getCategorias().stream().map(c -> c.getNome()).collect(Collectors.toSet())
                        : Set.of()
        );
    }
}
