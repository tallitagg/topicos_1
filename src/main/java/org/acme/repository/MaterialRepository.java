package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Material;

@ApplicationScoped
public class MaterialRepository implements PanacheRepository<Material> {

    public Material findByTipo(String tipo) {
        return find("UPPER(tipo) LIKE ?1", "%" + tipo.toUpperCase() + "%").firstResult();
    }

}
