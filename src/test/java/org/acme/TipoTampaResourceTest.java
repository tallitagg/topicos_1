package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.acme.dto.TipoTampaDTO;
import org.acme.dto.TipoTampaResponseDTO;
import org.acme.service.TipoTampaService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class TipoTampaResourceTest {

    @Inject
    TipoTampaService tipoTampaService;

    @Test
    void buscarTudoTest() {
        RestAssured.given()
                .when()
                .get("/tipoTampas")
                .then()
                .statusCode(200);
    }

    @Test
    void buscarPorDescricaoTest() {
        RestAssured.given()
                .pathParam("descricao", "Flip-top")
                .when()
                .get("/tipoTampas/descricao/{descricao}", "Flip-top")
                .then()
                .statusCode(200)
                .body("descricao", CoreMatchers.hasItem("Flip-top"));
    }

    @Test
    void buscarPorMaterialTest() {
        RestAssured.given()
                .pathParam("material", "PP/Tritan")
                .when()
                .get("/tipoTampas/material/{material}", "PP/Tritan")
                .then()
                .statusCode(200)
                .body("material", CoreMatchers.hasItem("PP/Tritan"));
    }

    @Test
    void incluirTest() {
        TipoTampaDTO dto = new TipoTampaDTO("Teste", "PP/Teste");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/tipoTampas")
                .then()
                .statusCode(201)
                .body("descricao", CoreMatchers.is("Teste"))
                .body("material", CoreMatchers.is("PP/Teste"));
    }

    @Test
    void atualizarTest() {
        TipoTampaDTO dto = new TipoTampaDTO("Teste2", "PP/Teste2");

        TipoTampaResponseDTO responseDTO = tipoTampaService.create(dto);

        TipoTampaDTO dtoUpdate = new TipoTampaDTO("Teste2", "PPL/Teste2");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when()
                .put("/tipoTampas/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = tipoTampaService.findById(responseDTO.id());
        assertEquals(dtoUpdate.material(), responseDTO.material());
        assertEquals(dtoUpdate.descricao(), responseDTO.descricao());
    }

    @Test
    void excluirTest() {
        TipoTampaDTO dto = new TipoTampaDTO("Teste3", "PPTeste");

        TipoTampaResponseDTO responseDTO = tipoTampaService.create(dto);

        RestAssured.given()
                .when()
                .delete("/tipoTampas/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = tipoTampaService.findById(responseDTO.id());

        assertNull(responseDTO);
    }
}
