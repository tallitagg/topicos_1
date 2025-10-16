package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.MarcaDTO;
import org.acme.dto.MarcaResponseDTO;
import org.acme.model.Marca;
import org.acme.model.Modelo;
import org.acme.repository.MarcaRepository;
import org.acme.repository.ModeloRepository;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MarcaServiceImpl implements MarcaService {

    @Inject
    MarcaRepository marcaRepository;

    @Inject
    ModeloRepository modeloRepository;

    @Override
    public List<MarcaResponseDTO> findAll() {
        return marcaRepository.listAll()
                .stream()
                .map(MarcaResponseDTO::valueOf)
                .toList();
    }

    @Override
    public List<MarcaResponseDTO> findByName(String name) {
        return marcaRepository.findByNome(name)
                .stream()
                .map(MarcaResponseDTO::valueOf)
                .toList();
    }

    @Override
    public MarcaResponseDTO findById(Long id) {
        return MarcaResponseDTO.valueOf(marcaRepository.findById(id));
    }

    @Override
    @Transactional
    public MarcaResponseDTO create(MarcaDTO dto) {
        Marca marca = new Marca();

        marca.setNome(dto.nome());

        marca.setModelos(new ArrayList<>());

        marcaRepository.persist(marca);

        if (dto.modeloIds() != null && !dto.modeloIds().isEmpty()) {
            List<Modelo> modelos = modeloRepository.find("id in ?1", dto.modeloIds()).list();
            for (Modelo m : modelos) {
                m.setMarca(marca);
            }
            marca.getModelos().addAll(modelos);
        }

        return MarcaResponseDTO.valueOf(marca);
    }

    @Override
    @Transactional
    public void update(Long id, MarcaDTO dto) {
        var marca = marcaRepository.findById(id);
        if (marca == null) throw new IllegalArgumentException("Marca não encontrada: " + id);

        marca.setNome(dto.nome());

        if (dto.modeloIds() != null) {
            List<Modelo> managed = marca.getModelos();
            for (Modelo m : managed) {
                m.setMarca(null);
            }
            managed.clear();

            if (!dto.modeloIds().isEmpty()) {
                var novosModelos = modeloRepository.find("id in ?1", dto.modeloIds()).list();
                for (Modelo m : novosModelos) {
                    m.setMarca(marca);
                }
                managed.addAll(novosModelos);
            }
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        marcaRepository.deleteById(id);
    }
}
