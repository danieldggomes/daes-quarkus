package com.example.previdencia.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.previdencia.model.PagamentoBeneficio;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

class PagamentoBeneficioResourceUnitTest {

    private final PagamentoBeneficioResource resource = new PagamentoBeneficioResource();

    @Test
    void createWhenBeneficioIsMissingReturnsBadRequest() {
        Response response = resource.create(new PagamentoBeneficio());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
