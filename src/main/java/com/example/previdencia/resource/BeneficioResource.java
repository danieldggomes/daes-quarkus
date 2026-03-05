package com.example.previdencia.resource;

import com.example.previdencia.model.Beneficio;
import com.example.previdencia.model.Cidadao;
import com.example.previdencia.model.PagamentoBeneficio;
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

@Path("/beneficios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BeneficioResource {

    @GET
    public List<Beneficio> list() {
        return Beneficio.listAll();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        Beneficio entity = Beneficio.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @POST
    @Transactional
    public Response create(Beneficio input) {
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
    public Response update(@PathParam("id") Long id, Beneficio input) {
        Beneficio entity = Beneficio.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Cidadao cidadao = resolveCidadao(input);
        if (cidadao == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("cidadao.id obrigatorio").build();
        }
        entity.tipoBeneficio = input.tipoBeneficio;
        entity.dataConcessao = input.dataConcessao;
        entity.valorMensal = input.valorMensal;
        entity.status = input.status;
        entity.cidadao = cidadao;
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        PanacheEntityBase entity = Beneficio.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("{id}/pagamentos")
    public List<PagamentoBeneficio> listPagamentos(@PathParam("id") Long id) {
        return PagamentoBeneficio.list("beneficio.id", id);
    }

    private Cidadao resolveCidadao(Beneficio input) {
        if (input == null || input.cidadao == null || input.cidadao.id == null) {
            return null;
        }
        return Cidadao.findById(input.cidadao.id);
    }
}
