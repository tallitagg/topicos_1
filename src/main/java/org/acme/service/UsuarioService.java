package org.acme.service;

import org.acme.model.Usuario;

import java.util.List;

public interface UsuarioService {
    List<Usuario> findAll();
    Usuario findByLogin(String login);
    Usuario findByLoginAndSenha(String login, String senha);
    Usuario findById(Long id);
}
