package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import org.acme.dto.ItemPedidoDTO;
import org.acme.dto.PedidoDTO;
import org.acme.dto.PedidoResponseDTO;
import org.acme.model.ItemPedido;
import org.acme.model.Pedido;
import org.acme.model.Produto;
import org.acme.model.Usuario;
import org.acme.repository.PedidoRepository;
import org.acme.repository.ProdutoRepository;
import org.acme.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {

    private static final Logger LOGGER = Logger.getLogger(CorServiceImpl.class.getName());

    @Inject
    PedidoRepository pedidoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    ProdutoRepository produtoRepository;


    @Override
    public List<PedidoResponseDTO> getAll() {
        LOGGER.info("Buscando todos os pedidos");
        List<Pedido> pedidos = pedidoRepository.listAll();
        LOGGER.info("Total de pedidos encontrados: " + pedidos.size());

        return pedidos.stream()
                .map(PedidoResponseDTO::valueOf)
                .toList();
    }

    @Override
    @Transactional
    public List<PedidoResponseDTO> findByUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username);
        return pedidoRepository.findByUsuario(usuario).stream().map(PedidoResponseDTO::valueOf).toList();
    }

    @Override
    @Transactional
    public PedidoResponseDTO create(PedidoDTO dto, String username) throws ConstraintViolationException {
        LOGGER.info("Criando novo pedido para usuário autenticado: " + username);

        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            LOGGER.severe("Usuário não encontrado para username: " + username);
            throw new WebApplicationException("Usuário não encontrado");
        }

        Pedido entity = new Pedido();
        entity.setDataPedido(LocalDateTime.now());
        entity.setUsuario(usuario);
        entity.setEnderecoEntrega(dto.enderecoEntrega());

        LOGGER.info("Forma de pagamento: " + dto.formaPagamento());
        entity.setFormaPagamento(dto.formaPagamento());

        List<ItemPedido> itensPedido = new ArrayList<>();
        double total = 0.0;

        for (ItemPedidoDTO itemDTO : dto.itensPedido()) {
            if (!dto.itensPedido().isEmpty()) {
                LOGGER.info("Quantidade de itens no pedido: " + dto.itensPedido().size());

                Produto produto = produtoRepository.findById(itemDTO.idProduto());
                if (produto == null) {
                    throw new WebApplicationException("Produto não encontrado: id=" + itemDTO.idProduto(), 404);
                }

                Integer estoqueAtual = produto.getEstoque();
                if (estoqueAtual == null) {
                    estoqueAtual = 0;
                }

                if (estoqueAtual < itemDTO.quantidade()) {
                    throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
                }

                produto.setEstoque(estoqueAtual - itemDTO.quantidade());

                ItemPedido ip = new ItemPedido();
                ip.setPedido(entity);
                ip.setProduto(produto);
                ip.setQuantidade(itemDTO.quantidade());
                ip.setPreco(produto.getPreco()); // preço unitário no momento da compra

                total += ip.getPreco() * ip.getQuantidade();

                itensPedido.add(ip);
            } else {
                LOGGER.warning("Pedido criado sem itens (lista de itensPedido vazia ou nula)");
            }

        }

        entity.setItensPedido(itensPedido);
        entity.setTotal(total);

        LOGGER.info("Persistindo pedido no banco...");
        pedidoRepository.persist(entity);
        LOGGER.info("Pedido persistido com sucesso. ID gerado: " + entity.getId());

        return PedidoResponseDTO.valueOf(entity);
    }
}
