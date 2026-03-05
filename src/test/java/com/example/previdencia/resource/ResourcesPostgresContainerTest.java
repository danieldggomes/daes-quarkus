package com.example.previdencia.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.example.previdencia.testsupport.PostgresTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@QuarkusTest
@QuarkusTestResource(value = PostgresTestResource.class, restrictToAnnotatedClass = true)
@Testcontainers(disabledWithoutDocker = true)
class ResourcesPostgresContainerTest {

    @Test
    void fullFlowWithRealPostgres() {
        Long cidadaoId = given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "cpf": "99999999999",
                        "nome": "Container User",
                        "dataNascimento": "1988-03-01",
                        "sexo": "OUTRO"
                      }
                      """)
                .when().post("/cidadaos")
                .then().statusCode(201)
                .extract().path("id");

        Long beneficioId = given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "tipoBeneficio": "AUXILIO_DOENCA",
                        "dataConcessao": "2025-12-01",
                        "valorMensal": 1400.00,
                        "status": "ATIVO",
                        "cidadao": {"id": %d}
                      }
                      """.formatted(cidadaoId))
                .when().post("/beneficios")
                .then().statusCode(201)
                .extract().path("id");

        Long contribuicaoId = given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "competencia": "2026-01",
                        "valor": 700.00,
                        "tipoContribuicao": "FACULTATIVO",
                        "cidadao": {"id": %d}
                      }
                      """.formatted(cidadaoId))
                .when().post("/contribuicoes")
                .then().statusCode(201)
                .extract().path("id");

        Long pagamentoId = given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "competencia": "2026-01",
                        "valorPago": 1350.00,
                        "dataPagamento": "2026-02-03",
                        "beneficio": {"id": %d}
                      }
                      """.formatted(beneficioId))
                .when().post("/pagamentos")
                .then().statusCode(201)
                .extract().path("id");

        given().when().get("/beneficios/{id}", beneficioId)
                .then().statusCode(200)
                .body("status", equalTo("ATIVO"));

        given().when().get("/contribuicoes/{id}", contribuicaoId)
                .then().statusCode(200)
                .body("tipoContribuicao", equalTo("FACULTATIVO"));

        given().when().get("/pagamentos/{id}", pagamentoId)
                .then().statusCode(200)
                .body("competencia", equalTo("2026-01"));
    }
}
