package com.example.previdencia.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.previdencia.model.Contribuicao;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

class ContribuicaoResourceUnitTest {

    private final ContribuicaoResource resource = new ContribuicaoResource();

    @Test
    void createWhenCidadaoIsMissingReturnsBadRequest() {
        Response response = resource.create(new Contribuicao());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
