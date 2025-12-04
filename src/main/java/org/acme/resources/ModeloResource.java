package org.acme.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.ModeloDTO;
import org.acme.service.ModeloService;

@Path("/modelos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModeloResource {

    @Inject
    ModeloService modeloService;

    @GET
    @RolesAllowed({"USER","ADM"})
    public Response buscarTodos() {
        return Response.ok(modeloService.findAll()).build();
    }

    @GET
    @RolesAllowed({"USER","ADM"})
    @Path("/nome/{nome}")
    public Response buscarPorNome(@PathParam("nome") String nome) {
        return Response.ok(modeloService.findByNome(nome)).build();
    }

    @GET
    @RolesAllowed({"USER","ADM"})
    @Path("/marca/{marcaNome}")
    public Response buscarPorMarca(@PathParam("marcaNome") String marca) {
        return Response.ok(modeloService.findByMarcaNome(marca)).build();
    }

    @GET
    @RolesAllowed({"USER","ADM"})
    @Path("/ano/{anoLancamento}")
    public Response buscarPorAnoLancamento(@PathParam("anoLancamento") Integer anoLancamento) {
        return Response.ok(modeloService.findByAnoLancamento(anoLancamento)).build();
    }

    @POST
    @RolesAllowed({"ADM"})
    public Response incluir(ModeloDTO dto) {
        return Response.status(Response.Status.CREATED).entity(modeloService.create(dto)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response atualizar(@PathParam("id") Long id, ModeloDTO dto) {
        modeloService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response deletar(@PathParam("id") Long id) {
        modeloService.delete(id);
        return Response.noContent().build();
    }
}
