package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.TipoTampaDTO;
import org.acme.service.TipoTampaService;

@Path("/tipoTampas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoTampaResource {

    @Inject
    TipoTampaService tipoTampaService;

    @GET
    public Response buscarTodos() {
        return Response.ok(tipoTampaService.findAll()).build();
    }

    @GET
    @Path("/descricao/{descricao}")
    public Response buscarPorDescricao(@PathParam("descricao") String descricao) {
        return Response.ok(tipoTampaService.findByDescricao(descricao)).build();
    }

    @GET
    @Path("/material/{material}")
    public Response buscarPorMaterial(@PathParam("material") String material) {
        return Response.ok(tipoTampaService.findByMaterial(material)).build();
    }

    @POST
    public Response incluir(TipoTampaDTO dto) {
        return Response.status(Response.Status.CREATED).entity(tipoTampaService.create(dto)).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, TipoTampaDTO dto) {
        tipoTampaService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        tipoTampaService.delete(id);
        return Response.noContent().build();
    }

}
