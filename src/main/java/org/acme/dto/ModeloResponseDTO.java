package org.acme.dto;

import org.acme.model.Modelo;

public record ModeloResponseDTO(
        Long id,
        String nome,
        Integer anoLancamento,
        MarcaResponseDTO marca
) {
    public static ModeloResponseDTO valueOf(Modelo modelo) {
        return new ModeloResponseDTO(
                modelo.getId(),
                modelo.getNome(),
                modelo.getAnoLancamento(),
                MarcaResponseDTO.valueOf(modelo.getMarca())
        );
    }
}
