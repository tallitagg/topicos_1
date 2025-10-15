package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.TipoIsolamento;

@ApplicationScoped
public class TipoIsolamentoRepository implements PanacheRepository<TipoIsolamento> {

    public TipoIsolamento findByDescricao(String descricao) {
        return find("UPPER(descricao) LIKE ?1", "%" + descricao.toUpperCase() + "%").firstResult();
    }

}
