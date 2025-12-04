package org.acme.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.MaterialDTO;
import org.acme.service.MaterialService;

@Path("/materiais")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaterialResource {

    @Inject
    MaterialService materialService;

    @GET
    @RolesAllowed({"USER","ADM"})
    public Response buscarTodos() {
        return Response.ok(materialService.findAll()).build();
    }

    @GET
    @Path("/{tipo}")
    @RolesAllowed({"USER","ADM"})
    public Response buscarPorTipo(@PathParam("tipo") String tipo) {
        return Response.ok(materialService.findByTipo(tipo)).build();
    }

    @POST
    @RolesAllowed({"ADM"})
    public Response incluir(MaterialDTO dto) {
        return Response.status(Response.Status.CREATED).entity(materialService.create(dto)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response atualizar(@PathParam("id") Long id, MaterialDTO dto) {
        materialService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response excluir(@PathParam("id") Long id) {
        materialService.delete(id);
        return Response.noContent().build();
    }
}
