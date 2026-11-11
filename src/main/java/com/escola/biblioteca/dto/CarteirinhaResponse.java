package com.escola.biblioteca.dto;

import com.escola.biblioteca.entity.Carteirinha;

import java.time.LocalDate;

public record CarteirinhaResponse(
        Long id,
        String numero,
        LocalDate dataEmissao,
        LocalDate dataValidade,
        String tipo,
        Long leitorId
) {
    public static CarteirinhaResponse from(Carteirinha carteirinha) {
        if (carteirinha == null) {
            return null;
        }
        return new CarteirinhaResponse(
                carteirinha.getId(),
                carteirinha.getNumero(),
                carteirinha.getDataEmissao(),
                carteirinha.getDataValidade(),
                carteirinha.getTipo(),
                carteirinha.getLeitor() != null ? carteirinha.getLeitor().getId() : null
        );
    }
}
