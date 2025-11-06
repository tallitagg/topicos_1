package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.acme.dto.MarcaDTO;
import org.acme.dto.MarcaResponseDTO;
import org.acme.service.MarcaService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class MarcaResourceTest {

    @Inject
    MarcaService marcaService;

    @Test
    void buscarTodosTest() {
        RestAssured.given()
                .when()
                .get("/marcas")
                .then()
                .statusCode(200);
    }

    @Test
    void buscarPorNome() {
        RestAssured.given()
                .pathParam("nome", "HydraPro")
                .when()
                .get("/marcas/{nome}")
                .then()
                .statusCode(200)
                .body("nome", CoreMatchers.hasItem("HydraPro"));
    }

    @Test
    void incluirTest() {
        MarcaDTO dto = new MarcaDTO("Stanley", List.of(2L, 1L));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/marcas")
                .then()
                .statusCode(201)
                .body("nome", CoreMatchers.is("Stanley"))
                .body("modelos.id", CoreMatchers.hasItems(1, 2));
    }

    @Test
    void alterarTest() {
        MarcaDTO dto = new MarcaDTO("Stanley", List.of(2L, 1L));
        MarcaResponseDTO responseDTO = marcaService.create(dto);

        MarcaDTO dtoUpdate = new MarcaDTO("Stanley 3D", List.of(4L));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when()
                .put("/marcas/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = marcaService.findById(responseDTO.id());
        assertEquals(dtoUpdate.nome(), responseDTO.nome());
        assertEquals(dtoUpdate.modeloIds(), responseDTO.modelos());
    }

    @Test
    void excluirTest() {
        MarcaDTO dto = new MarcaDTO("HotCool", List.of(10L));
        MarcaResponseDTO responseDTO = marcaService.create(dto);

        RestAssured.given()
                .when()
                .delete("/marcas/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = marcaService.findById(responseDTO.id());
        assertNull(responseDTO);
    }


}
