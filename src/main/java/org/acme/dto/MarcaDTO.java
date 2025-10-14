package org.acme.dto;

import jakarta.validation.constraints.NotBlank;

public record MarcaDTO(
        @NotBlank
        String nome
) {
}
