package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.ProdutoDTO;
import org.acme.service.ProdutoService;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoResource {

    @Inject
    ProdutoService produtoService;

    @GET
    public Response buscarTodos() {
        return Response.ok(produtoService.findAll()).build();
    }

    @GET
    @Path("/nome/{nome}")
    public Response buscarPorNome(@PathParam("nome") String nome) {
        return Response.ok(produtoService.findByNome(nome)).build();
    }

    @GET
    @Path("/preco/{preco}")
    public Response buscarPorPreco(@PathParam("preco") Long preco) {
        return Response.ok(produtoService.findByPreco(preco)).build();
    }

    @GET
    @Path("/marca/{marca}")
    public Response buscarPorMarca(@PathParam("marca") String marca) {
        return Response.ok(produtoService.findByMarca(marca)).build();
    }

    @GET
    @Path("modelo/{modelo}")
    public Response buscarPorModelo(@PathParam("modelo") String modelo) {
        return Response.ok(produtoService.findByModelo(modelo)).build();
    }

    @GET
    @Path("material/{material}")
    public Response buscarPorMaterial(@PathParam("material") String material) {
        return Response.ok(produtoService.findByMaterial(material)).build();
    }

    @GET
    @Path("capacidade/{capacidade}")
    public Response buscarPorCapacidade(@PathParam("capacidade") Double capacidade) {
        return Response.ok(produtoService.findByCapacidade(capacidade)).build();
    }

    @POST
    public Response incluir(ProdutoDTO dto) {
        return Response.status(Response.Status.CREATED).entity(produtoService.create(dto)).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, ProdutoDTO dto) {
        produtoService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        produtoService.delete(id);
        return Response.noContent().build();
    }
}
