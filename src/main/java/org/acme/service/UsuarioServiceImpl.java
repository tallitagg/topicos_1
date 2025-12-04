package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.model.Usuario;
import org.acme.repository.UsuarioRepository;

import java.util.List;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {
    @Inject
    UsuarioRepository repository;

    @Override
    public List<Usuario> findAll() {
        return repository
                .listAll();
    }

    @Override
    public Usuario findByLogin(String login) {
        return repository
                .findByLogin(login);
    }

    @Override
    public Usuario findByLoginAndSenha(String login, String senha) {
        return repository
                .findByLoginSenha(login, senha);
    }

    @Override
    public Usuario findById(Long id) {
        Usuario usuario = repository.findById(id);

        if (usuario == null)
            return null;

        return usuario;
    }
}
