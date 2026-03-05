package com.example.previdencia.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

class CidadaoResourceUnitTest {

    private final CidadaoResource resource = new CidadaoResource();

    @Test
    void createWhenInputNullReturnsBadRequest() {
        Response response = resource.create(null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }
}
