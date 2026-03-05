package com.example.previdencia.resource;

import com.example.previdencia.model.Cidadao;
import com.example.previdencia.model.Contribuicao;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/contribuicoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContribuicaoResource {

    @GET
    public List<Contribuicao> list() {
        return Contribuicao.listAll();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        Contribuicao entity = Contribuicao.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @POST
    @Transactional
    public Response create(Contribuicao input) {
        Cidadao cidadao = resolveCidadao(input);
        if (cidadao == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("cidadao.id obrigatorio").build();
        }
        input.cidadao = cidadao;
        input.persist();
        return Response.status(Response.Status.CREATED).entity(input).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, Contribuicao input) {
        Contribuicao entity = Contribuicao.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Cidadao cidadao = resolveCidadao(input);
        if (cidadao == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("cidadao.id obrigatorio").build();
        }
        entity.competencia = input.competencia;
        entity.valor = input.valor;
        entity.tipoContribuicao = input.tipoContribuicao;
        entity.cidadao = cidadao;
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        PanacheEntityBase entity = Contribuicao.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.delete();
        return Response.noContent().build();
    }

    private Cidadao resolveCidadao(Contribuicao input) {
        if (input == null || input.cidadao == null || input.cidadao.id == null) {
            return null;
        }
        return Cidadao.findById(input.cidadao.id);
    }
}
