package com.example.previdencia.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.previdencia.model.Beneficio;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

class BeneficioResourceUnitTest {

    private final BeneficioResource resource = new BeneficioResource();

    @Test
    void createWhenCidadaoIsMissingReturnsBadRequest() {
        Response response = resource.create(new Beneficio());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
