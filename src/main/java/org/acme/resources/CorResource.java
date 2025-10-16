package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CorDTO;
import org.acme.service.CorService;

@Path("/cores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CorResource {

    @Inject
    CorService corService;

    @GET
    public Response buscarTodas() {
        return Response.ok(corService.findAll()).build();
    }

    @GET
    @Path("/{nome}")
    public Response buscarPorNome(@PathParam("nome") String nome) {
        return Response.ok(corService.findByName(nome)).build();
    }

    @POST
    public Response incluir(CorDTO dto) {
        return Response.status(Response.Status.CREATED).entity(corService.create(dto)).build();
    }

    @PUT
    @Path("/{id}")
    public Response alterar(@PathParam("id") Long id, CorDTO dto) {
        corService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        corService.delete(id);
        return Response.noContent().build();
    }

}
