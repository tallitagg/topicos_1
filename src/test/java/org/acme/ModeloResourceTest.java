package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.acme.dto.ModeloDTO;
import org.acme.dto.ModeloResponseDTO;
import org.acme.service.ModeloService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class ModeloResourceTest {

    @Inject
    ModeloService modeloService;

    @Test
    void buscarTodosTest() {
        RestAssured.given()
                .when()
                .get("/modelos")
                .then()
                .statusCode(200);
    }

    @Test
    void buscarPorNome() {
        RestAssured.given()
                .pathParam("nome", "SteelCore 750")
                .when()
                .get("/modelos/nome/{nome}")
                .then()
                .statusCode(200)
                .body("nome", CoreMatchers.hasItem("SteelCore 750"));
    }

    // TODO corrigir
    @Test
    void buscarPorMarca() {
        RestAssured.given()
                .pathParam("marcaNome", "HydraPro")
                .when()
                .get("/modelos/marca/{marcaNome}")
                .then()
                .statusCode(200)
                .body("marca.nome", CoreMatchers.hasItem("HydraPro"));
    }

    @Test
    void buscarPorAnoLancamento() {
        RestAssured.given()
                .pathParam("anoLancamento", 2023)
                .when()
                .get("/modelos/ano/{anoLancamento}")
                .then()
                .statusCode(200)
                .body("anoLancamento", CoreMatchers.hasItem(2023));
    }

    @Test
    void incluirTest() {
        ModeloDTO dto = new ModeloDTO("Teste", 2022,2L);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/modelos")
                .then()
                .statusCode(201)
                .body("nome", CoreMatchers.is("Teste"))
                .body("anoLancamento", CoreMatchers.is(2022));
    }

    @Test
    void updateTest() {
        ModeloDTO dto = new ModeloDTO("Teste2", 2023, 3L);

        ModeloResponseDTO responseDTO = modeloService.create(dto);

        ModeloDTO dtoUpdate = new ModeloDTO("Teste2", 2024, 3L);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when()
                .put("/modelos/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = modeloService.findById(responseDTO.id());
        assertEquals(dtoUpdate.nome(), responseDTO.nome());
        assertEquals(dtoUpdate.anoLancamento(), responseDTO.anoLancamento());
    }

    @Test
    void deleteTest() {
        ModeloDTO dto = new ModeloDTO("Teste3", 2024, 2L);
        ModeloResponseDTO responseDTO = modeloService.create(dto);

        RestAssured.given()
                .when()
                .delete("/modelos/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = modeloService.findById(responseDTO.id());

        assertNull(responseDTO);
    }

}
