package com.example.previdencia.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class ResourcesFunctionalTest {

    @Test
    void cidadaoEndpointCrud() {
        long cidadaoId = extractId(given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "cpf": "12345678901",
                        "nome": "Joao Teste",
                        "dataNascimento": "1980-01-01",
                        "sexo": "MASCULINO"
                      }
                      """)
                .when().post("/cidadaos")
                .then().statusCode(201)
                .extract().path("id"));

        given().when().get("/cidadaos/{id}", cidadaoId)
                .then().statusCode(200)
                .body("id", equalTo((int) cidadaoId));

        given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "cpf": "12345678901",
                        "nome": "Joao Atualizado",
                        "dataNascimento": "1980-01-01",
                        "sexo": "MASCULINO"
                      }
                      """)
                .when().put("/cidadaos/{id}", cidadaoId)
                .then().statusCode(200)
                .body("nome", equalTo("Joao Atualizado"));

        given().when().delete("/cidadaos/{id}", cidadaoId)
                .then().statusCode(204);
    }

    @Test
    void beneficioEndpointCrud() {
        long cidadaoId = createCidadao("12345678902", "Maria Beneficio");

        long beneficioId = extractId(given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "tipoBeneficio": "APOSENTADORIA_IDADE",
                        "dataConcessao": "2025-01-01",
                        "valorMensal": 2500.50,
                        "status": "ATIVO",
                        "cidadao": {"id": %d}
                      }
                      """.formatted(cidadaoId))
                .when().post("/beneficios")
                .then().statusCode(201)
                .extract().path("id"));

        given().when().get("/beneficios/{id}", beneficioId)
                .then().statusCode(200)
                .body("id", equalTo((int) beneficioId));

        given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "tipoBeneficio": "APOSENTADORIA_TEMPO_CONTRIBUICAO",
                        "dataConcessao": "2025-02-01",
                        "valorMensal": 3000.00,
                        "status": "SUSPENSO",
                        "cidadao": {"id": %d}
                      }
                      """.formatted(cidadaoId))
                .when().put("/beneficios/{id}", beneficioId)
                .then().statusCode(200)
                .body("status", equalTo("SUSPENSO"));

        given().when().delete("/beneficios/{id}", beneficioId)
                .then().statusCode(204);
    }

    @Test
    void contribuicaoEndpointCrud() {
        long cidadaoId = createCidadao("12345678903", "Carlos Contribuicao");

        long contribuicaoId = extractId(given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "competencia": "2026-01",
                        "valor": 450.75,
                        "tipoContribuicao": "EMPREGADO",
                        "cidadao": {"id": %d}
                      }
                      """.formatted(cidadaoId))
                .when().post("/contribuicoes")
                .then().statusCode(201)
                .extract().path("id"));

        given().when().get("/contribuicoes/{id}", contribuicaoId)
                .then().statusCode(200)
                .body("id", equalTo((int) contribuicaoId));

        given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "competencia": "2026-02",
                        "valor": 600.00,
                        "tipoContribuicao": "AUTONOMO",
                        "cidadao": {"id": %d}
                      }
                      """.formatted(cidadaoId))
                .when().put("/contribuicoes/{id}", contribuicaoId)
                .then().statusCode(200)
                .body("tipoContribuicao", equalTo("AUTONOMO"));

        given().when().delete("/contribuicoes/{id}", contribuicaoId)
                .then().statusCode(204);
    }

    @Test
    void pagamentoEndpointCrud() {
        long cidadaoId = createCidadao("12345678904", "Paulo Pagamento");
        long beneficioId = createBeneficio(cidadaoId);

        long pagamentoId = extractId(given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "competencia": "2026-01",
                        "valorPago": 1800.00,
                        "dataPagamento": "2026-02-05",
                        "beneficio": {"id": %d}
                      }
                      """.formatted(beneficioId))
                .when().post("/pagamentos")
                .then().statusCode(201)
                .extract().path("id"));

        given().when().get("/pagamentos/{id}", pagamentoId)
                .then().statusCode(200)
                .body("id", equalTo((int) pagamentoId));

        given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "competencia": "2026-02",
                        "valorPago": 1900.00,
                        "dataPagamento": "2026-03-05",
                        "beneficio": {"id": %d}
                      }
                      """.formatted(beneficioId))
                .when().put("/pagamentos/{id}", pagamentoId)
                .then().statusCode(200)
                .body("competencia", equalTo("2026-02"));

        given().when().get("/beneficios/{id}/pagamentos", beneficioId)
                .then().statusCode(200);

        given().when().delete("/pagamentos/{id}", pagamentoId)
                .then().statusCode(204);
    }

    private long createCidadao(String cpf, String nome) {
        return extractId(given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "cpf": "%s",
                        "nome": "%s",
                        "dataNascimento": "1985-05-15",
                        "sexo": "FEMININO"
                      }
                      """.formatted(cpf, nome))
                .when().post("/cidadaos")
                .then().statusCode(201)
                .extract().path("id"));
    }

    private long createBeneficio(long cidadaoId) {
        return extractId(given()
                .contentType(ContentType.JSON)
                .body("""
                      {
                        "tipoBeneficio": "PENSAO_MORTE",
                        "dataConcessao": "2025-01-01",
                        "valorMensal": 1800.00,
                        "status": "ATIVO",
                        "cidadao": {"id": %d}
                      }
                      """.formatted(cidadaoId))
                .when().post("/beneficios")
                .then().statusCode(201)
                .extract().path("id"));
    }

    private long extractId(Object id) {
        return ((Number) id).longValue();
    }
}
