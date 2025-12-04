package org.acme.dto;

import org.acme.model.Usuario;

public record AuthResponseDTO(
        Long id,
        String login,
        String senha
) {
    public static AuthResponseDTO valueOf(Usuario usuario) {
        return new AuthResponseDTO(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getSenha()
        );
    }
}
