package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.TipoTampa;

@ApplicationScoped
public class TipoTampaRepository implements PanacheRepository<TipoTampa> {

    public TipoTampa findByDescricao(String descricao) {
        return find("UPPER(descricao) LIKE ?1", "%" + descricao.toUpperCase() + "%").firstResult();
    }

}
