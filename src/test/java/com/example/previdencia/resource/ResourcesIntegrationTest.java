package com.example.previdencia.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.previdencia.model.Beneficio;
import com.example.previdencia.model.Cidadao;
import com.example.previdencia.model.Contribuicao;
import com.example.previdencia.model.PagamentoBeneficio;
import com.example.previdencia.testsupport.TestDataFactory;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ResourcesIntegrationTest {

    @Inject
    CidadaoResource cidadaoResource;

    @Inject
    BeneficioResource beneficioResource;

    @Inject
    ContribuicaoResource contribuicaoResource;

    @Inject
    PagamentoBeneficioResource pagamentoBeneficioResource;

    @Test
    @TestTransaction
    void cidadaoCrudByResourceBean() {
        Cidadao novo = TestDataFactory.cidadao();

        Response created = cidadaoResource.create(novo);
        assertEquals(201, created.getStatus());

        Cidadao persisted = (Cidadao) created.getEntity();
        assertNotNull(persisted.id);

        Cidadao update = TestDataFactory.cidadao();
        Response updated = cidadaoResource.update(persisted.id, update);
        assertEquals(200, updated.getStatus());

        Response loaded = cidadaoResource.get(persisted.id);
        assertEquals(200, loaded.getStatus());

        Response deleted = cidadaoResource.delete(persisted.id);
        assertEquals(204, deleted.getStatus());
    }

    @Test
    @TestTransaction
    void beneficioCrudByResourceBean() {
        Cidadao cidadao = TestDataFactory.cidadao();
        cidadao.persist();

        Beneficio novo = TestDataFactory.beneficio(cidadao);
        Response created = beneficioResource.create(novo);
        assertEquals(201, created.getStatus());

        Beneficio persisted = (Beneficio) created.getEntity();
        assertNotNull(persisted.id);

        Beneficio update = TestDataFactory.beneficio(cidadao);
        Response updated = beneficioResource.update(persisted.id, update);
        assertEquals(200, updated.getStatus());

        Response loaded = beneficioResource.get(persisted.id);
        assertEquals(200, loaded.getStatus());

        Response deleted = beneficioResource.delete(persisted.id);
        assertEquals(204, deleted.getStatus());
    }

    @Test
    @TestTransaction
    void contribuicaoCrudByResourceBean() {
        Cidadao cidadao = TestDataFactory.cidadao();
        cidadao.persist();

        Contribuicao nova = TestDataFactory.contribuicao(cidadao);
        Response created = contribuicaoResource.create(nova);
        assertEquals(201, created.getStatus());

        Contribuicao persisted = (Contribuicao) created.getEntity();
        assertNotNull(persisted.id);

        Contribuicao update = TestDataFactory.contribuicao(cidadao);
        Response updated = contribuicaoResource.update(persisted.id, update);
        assertEquals(200, updated.getStatus());

        Response loaded = contribuicaoResource.get(persisted.id);
        assertEquals(200, loaded.getStatus());

        Response deleted = contribuicaoResource.delete(persisted.id);
        assertEquals(204, deleted.getStatus());
    }

    @Test
    @TestTransaction
    void pagamentoCrudByResourceBean() {
        Cidadao cidadao = TestDataFactory.cidadao();
        cidadao.persist();

        Beneficio beneficio = TestDataFactory.beneficio(cidadao);
        beneficio.persist();

        PagamentoBeneficio novo = TestDataFactory.pagamento(beneficio);
        Response created = pagamentoBeneficioResource.create(novo);
        assertEquals(201, created.getStatus());

        PagamentoBeneficio persisted = (PagamentoBeneficio) created.getEntity();
        assertNotNull(persisted.id);

        PagamentoBeneficio update = TestDataFactory.pagamento(beneficio);
        Response updated = pagamentoBeneficioResource.update(persisted.id, update);
        assertEquals(200, updated.getStatus());

        Response loaded = pagamentoBeneficioResource.get(persisted.id);
        assertEquals(200, loaded.getStatus());

        Response deleted = pagamentoBeneficioResource.delete(persisted.id);
        assertEquals(204, deleted.getStatus());
    }
}
