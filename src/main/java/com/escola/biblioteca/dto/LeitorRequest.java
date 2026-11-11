package com.escola.biblioteca.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LeitorRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        String telefone,

        LocalDate dataNascimento,

        @Valid
        @NotNull(message = "A carteirinha é obrigatória")
        CarteirinhaRequest carteirinha
) {
}
