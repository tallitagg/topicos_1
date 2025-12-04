package org.acme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Usuario extends DefaultEntity{

    private String nome;

    @Column(unique = true)
    private String login;
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
