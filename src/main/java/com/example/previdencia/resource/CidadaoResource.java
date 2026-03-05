package com.example.previdencia.resource;

import com.example.previdencia.model.Beneficio;
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
import java.time.LocalDate;
import java.util.List;

@Path("/cidadaos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CidadaoResource {

    @GET
    public List<Cidadao> list() {
        return Cidadao.listAll();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        Cidadao entity = Cidadao.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entity).build();
    }

    @POST
    @Transactional
    public Response create(Cidadao input) {
        if (input == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (input.dataCadastro == null) {
            input.dataCadastro = LocalDate.now();
        }
        input.persist();
        return Response.status(Response.Status.CREATED).entity(input).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, Cidadao input) {
        Cidadao entity = Cidadao.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.cpf = input.cpf;
        entity.nome = input.nome;
        entity.dataNascimento = input.dataNascimento;
        entity.sexo = input.sexo;
        if (input.dataCadastro != null) {
            entity.dataCadastro = input.dataCadastro;
        }
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        PanacheEntityBase entity = Cidadao.findById(id);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entity.delete();
        return Response.noContent().build();
    }

    @GET
    @Path("{id}/beneficios")
    public List<Beneficio> listBeneficios(@PathParam("id") Long id) {
        return Beneficio.list("cidadao.id", id);
    }

    @GET
    @Path("{id}/contribuicoes")
    public List<Contribuicao> listContribuicoes(@PathParam("id") Long id) {
        return Contribuicao.list("cidadao.id", id);
    }
}
