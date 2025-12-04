package org.acme.resources;

import jakarta.annotation.security.RolesAllowed;
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
    @RolesAllowed({"USER", "ADM"})
    public Response buscarTodas() {
        return Response.ok(corService.findAll()).build();
    }

    @GET
    @Path("/{nome}")
    @RolesAllowed({"USER", "ADM"})
    public Response buscarPorNome(@PathParam("nome") String nome) {
        return Response.ok(corService.findByName(nome)).build();
    }

    @POST
    @RolesAllowed({"ADM"})
    public Response incluir(CorDTO dto) {
        return Response.status(Response.Status.CREATED).entity(corService.create(dto)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response alterar(@PathParam("id") Long id, CorDTO dto) {
        corService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response excluir(@PathParam("id") Long id) {
        corService.delete(id);
        return Response.noContent().build();
    }

}
