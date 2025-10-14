package org.acme.dto;

import org.acme.model.Marca;

public record MarcaResponseDTO(
        Long id,
        String nome
) {
    public static MarcaResponseDTO valueOf(Marca marca) {
        return new MarcaResponseDTO(
                marca.getId(),
                marca.getNome()
        );
    }
}
