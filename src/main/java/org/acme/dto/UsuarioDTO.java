package org.acme.dto;

public record UsuarioDTO(
        String login,
        String nome,
        String username,
        String senha,
        int idPerfil
) {
}
