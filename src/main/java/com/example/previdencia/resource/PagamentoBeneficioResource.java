package com.example.previdencia.resource;

import com.example.previdencia.model.Beneficio;
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

@Path("/pagamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PagamentoBeneficioResource {

    @GET
    public List<PagamentoBeneficio> list() {
        return PagamentoBeneficio.listAll();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        PagamentoBeneficio entity = PagamentoBeneficio.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @POST
    @Transactional
    public Response create(PagamentoBeneficio input) {
        Beneficio beneficio = resolveBeneficio(input);
        if (beneficio == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("beneficio.id obrigatorio").build();
        }
        input.beneficio = beneficio;
        input.persist();
        return Response.status(Response.Status.CREATED).entity(input).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, PagamentoBeneficio input) {
        PagamentoBeneficio entity = PagamentoBeneficio.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Beneficio beneficio = resolveBeneficio(input);
        if (beneficio == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("beneficio.id obrigatorio").build();
        }
        entity.competencia = input.competencia;
        entity.valorPago = input.valorPago;
        entity.dataPagamento = input.dataPagamento;
        entity.beneficio = beneficio;
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        PanacheEntityBase entity = PagamentoBeneficio.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.delete();
        return Response.noContent().build();
    }

    private Beneficio resolveBeneficio(PagamentoBeneficio input) {
        if (input == null || input.beneficio == null || input.beneficio.id == null) {
            return null;
        }
        return Beneficio.findById(input.beneficio.id);
    }
}
