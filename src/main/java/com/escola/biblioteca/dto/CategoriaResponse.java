package com.escola.biblioteca.dto;

import com.escola.biblioteca.entity.Categoria;

public record CategoriaResponse(
        Long id,
        String nome,
        String descricao,
        int quantidadeLivros
) {
    public static CategoriaResponse from(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getLivros() != null ? categoria.getLivros().size() : 0
        );
    }
}
