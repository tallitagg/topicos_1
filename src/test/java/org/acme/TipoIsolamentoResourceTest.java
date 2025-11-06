package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.acme.dto.TipoIsolamentoDTO;
import org.acme.dto.TipoIsolamentoResponseDTO;
import org.acme.service.TipoIsolamentoService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class TipoIsolamentoResourceTest {

    @Inject
    TipoIsolamentoService tipoIsolamentoService;

    @Test
    void buscarTodosTest() {
        RestAssured.given()
                .when()
                .get("/tipoIsolamentos")
                .then()
                .statusCode(200);
    }

    @Test
    void buscarPorDescricaoTest() {
        RestAssured.given()
                .pathParam("descricao", "Gel Pack")
                .when()
                .get("tipoIsolamentos/descricao/{descricao}")
                .then()
                .statusCode(200)
                .body("descricao", CoreMatchers.hasItem("Gel Pack"));
    }

    @Test
    void buscarPorEficienciaTermicaTest() {
        RestAssured.given()
                .pathParam("eficienciaTermica", 92)
                .when()
                .get("/tipoIsolamentos/eficienciaTermica/{eficienciaTermica}")
                .then()
                .statusCode(200)
                .body("eficienciaTermica", CoreMatchers.hasItem(92F));
    }

    @Test
    void incluirTest() {
        TipoIsolamentoDTO dto = new TipoIsolamentoDTO("Testando", 30.0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/tipoIsolamentos/")
                .then()
                .statusCode(201)
                .body("descricao", CoreMatchers.is("Testando"))
                .body("eficienciaTermica", CoreMatchers.is(30F));
    }

    @Test
    void atualizarTest() {
        TipoIsolamentoDTO dto = new TipoIsolamentoDTO("Testando2", 31.0);

        TipoIsolamentoResponseDTO responseDTO = tipoIsolamentoService.create(dto);

        TipoIsolamentoDTO dtoUpdate = new TipoIsolamentoDTO("Testando3", 31.0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when()
                .put("/tipoIsolamentos/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = tipoIsolamentoService.findById(responseDTO.id());
        assertEquals(dtoUpdate.descricao(), responseDTO.descricao());
        assertEquals(dtoUpdate.eficienciaTermica(), responseDTO.eficienciaTermica());
    }

    @Test
    void excluirTest() {
        TipoIsolamentoDTO dto = new TipoIsolamentoDTO("Testando4", 31.0);

        TipoIsolamentoResponseDTO responseDTO = tipoIsolamentoService.create(dto);

        RestAssured.given()
                .when()
                .delete("/tipoIsolamentos/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = tipoIsolamentoService.findById(responseDTO.id());

        assertNull(responseDTO);
    }
}
