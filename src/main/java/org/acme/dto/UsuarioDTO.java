package org.acme.dto;

public record UsuarioDTO(
        String login,
        String senha,
        int idPerfil
) {
}
