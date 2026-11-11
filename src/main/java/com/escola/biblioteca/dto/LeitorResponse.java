package com.escola.biblioteca.dto;

import com.escola.biblioteca.entity.Leitor;

import java.time.LocalDate;

public record LeitorResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        LocalDate dataNascimento,
        boolean ativo,
        CarteirinhaResponse carteirinha,
        int totalEmprestimos
) {
    public static LeitorResponse from(Leitor leitor) {
        return new LeitorResponse(
                leitor.getId(),
                leitor.getNome(),
                leitor.getEmail(),
                leitor.getTelefone(),
                leitor.getDataNascimento(),
                leitor.isAtivo(),
                CarteirinhaResponse.from(leitor.getCarteirinha()),
                leitor.getEmprestimos() != null ? leitor.getEmprestimos().size() : 0
        );
    }
}
