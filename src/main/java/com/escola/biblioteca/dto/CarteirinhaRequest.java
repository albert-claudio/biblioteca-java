package com.escola.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CarteirinhaRequest(
        @NotBlank(message = "O número da carteirinha é obrigatório")
        String numero,

        @NotNull(message = "A data de emissão é obrigatória")
        LocalDate dataEmissao,

        @NotNull(message = "A data de validade é obrigatória")
        LocalDate dataValidade,

        String tipo
) {
}
