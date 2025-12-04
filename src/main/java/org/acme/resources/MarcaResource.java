package org.acme.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.MarcaDTO;
import org.acme.service.MarcaService;

@Path("/marcas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarcaResource {

    @Inject
    MarcaService marcaService;

    @GET
    @RolesAllowed({"USER", "ADM"})
    public Response buscarTodas() {
        return Response.ok(marcaService.findAll()).build();
    }

    @GET
    @Path("/{nome}")
    @RolesAllowed({"USER", "ADM"})
    public Response buscarPorNome(@PathParam("nome") String nome) {
        return Response.ok(marcaService.findByName(nome)).build();
    }

    @POST
    @RolesAllowed({"ADM"})
    public Response incluir(MarcaDTO dto) {
        return Response.status(Response.Status.CREATED).entity(marcaService.create(dto)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response alterar(@PathParam("id") Long id, MarcaDTO dto) {
        marcaService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response excluir(@PathParam("id") Long id) {
        marcaService.delete(id);
        return Response.noContent().build();
    }

}
