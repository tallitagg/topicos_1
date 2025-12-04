package org.acme.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.TipoIsolamentoDTO;
import org.acme.service.TipoIsolamentoService;

@Path("/tipoIsolamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipoIsolamentoResource {

    @Inject
    TipoIsolamentoService tipoIsolamentoService;

    @GET
    @RolesAllowed({"USER","ADM"})
    public Response buscarTodos() {
        return Response.ok(tipoIsolamentoService.findAll()).build();
    }

    @GET
    @RolesAllowed({"USER","ADM"})
    @Path("/descricao/{descricao}")
    public Response buscarPorDescricao(@PathParam("descricao") String descricao) {
        return Response.ok(tipoIsolamentoService.findByDescricao(descricao)).build();
    }

    @GET
    @RolesAllowed({"USER","ADM"})
    @Path("/eficienciaTermica/{eficienciaTermica}")
    public Response buscarPorEficienciaTermica(@PathParam("eficienciaTermica") Long eficienciaTermica) {
        return Response.ok(tipoIsolamentoService.findByEficienciaTermica(eficienciaTermica)).build();
    }

    @POST
    @RolesAllowed({"ADM"})
    public Response incluir(TipoIsolamentoDTO dto) {
        return Response.status(Response.Status.CREATED).entity(tipoIsolamentoService.create(dto)).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADM"})
    public Response atualizar(@PathParam("id") Long id, TipoIsolamentoDTO dto) {
        tipoIsolamentoService.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @RolesAllowed({"ADM"})
    @Path("/{id}")
    public Response excluir(@PathParam("id") Long id) {
        tipoIsolamentoService.delete(id);
        return Response.noContent().build();
    }
}
