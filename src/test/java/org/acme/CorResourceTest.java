package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.acme.dto.CorDTO;
import org.acme.dto.CorResponseDTO;
import org.acme.service.CorService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class CorResourceTest {

    @Inject
    CorService corService;

    @Test
    void buscarTodosTest() {
        RestAssured.given()
                .when()
                .get("/cores")
                .then()
                .statusCode(200);
    }

    @Test
    void buscarPorNomeTest() {
        RestAssured.given()
                .pathParam("nome", "Preto Fosco")
                .when()
                .get("/cores/{nome}")
                .then()
                .statusCode(200)
                .body("nome", CoreMatchers.hasItem("Preto Fosco"));
    }

    @Test
    void incluirTest() {
        CorDTO dto = new CorDTO("Alaranjado", "#FFA500");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/cores")
                .then()
                .statusCode(201)
                .body("nome", CoreMatchers.is("Alaranjado"));
    }

    @Test
    void alterarTest() {
        CorDTO dto = new CorDTO("Alaranjado", "#FFA450");

        CorResponseDTO responseDTO = corService.create(dto);

        CorDTO dtoUpdate = new CorDTO("Laranja Alaranjado", "#FFA500");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when()
                .put("/cores/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = corService.findById(responseDTO.id());
        assertEquals(dtoUpdate.nome(), responseDTO.nome());
        assertEquals(dtoUpdate.codigoHex(), responseDTO.codigoHex());
    }

    @Test
    void excluirTest() {
        CorDTO dto = new CorDTO("Esverdeado", "#00FF00");

        CorResponseDTO responseDTO = corService.create(dto);

        RestAssured.given()
                .when()
                .delete("/cores/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = corService.findById(responseDTO.id());

        assertNull(responseDTO);
    }

}
