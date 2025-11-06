package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.acme.dto.MaterialDTO;
import org.acme.dto.MaterialResponseDTO;
import org.acme.service.MaterialService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class MaterialResourceTest {

    @Inject
    MaterialService materialService;

    @Test
    void buscarTodosTest() {
        RestAssured.given()
                .when()
                .get("/materiais")
                .then()
                .statusCode(200);
    }

    @Test
    void buscarPorTipoTest() {
        RestAssured.given()
                .pathParam("tipo", "Aço Inox 316L (corpo interno, alta resistência à corrosão)")
                .when()
                .get("/materiais/{tipo}")
                .then()
                .statusCode(200)
                .body("tipo", CoreMatchers.hasItem("Aço Inox 316L (corpo interno, alta resistência à corrosão)"));
    }

    @Test
    void incluirTest() {
        MaterialDTO dto = new MaterialDTO("Aço Inox 320L", 250.0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/materiais")
                .then()
                .statusCode(201)
                .body("tipo", CoreMatchers.is("Aço Inox 320L"))
                .body("resistenciaTemperatura", CoreMatchers.is(250.0F));
    }

    @Test
    void atualizarTest() {
        MaterialDTO dto = new MaterialDTO("Aço Inox 316L (corpo interno, alta resistência à corrosão)", 160.0);

        MaterialResponseDTO responseDTO = materialService.create(dto);

        MaterialDTO dtoUpdate = new MaterialDTO("Aço Inox 320L", 250.0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when()
                .put("/materiais/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = materialService.findById(responseDTO.id());
        assertEquals(dtoUpdate.tipo(), responseDTO.tipo());
        assertEquals(dtoUpdate.resistenciaTemperatura(), responseDTO.resistenciaTemperatura());
    }

    @Test
    void excluirTest() {
        MaterialDTO dto = new MaterialDTO("Aço Inox 320L", 250.0);
        MaterialResponseDTO responseDTO = materialService.create(dto);

        RestAssured.given()
                .when()
                .delete("/materiais/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = materialService.findById(responseDTO.id());

        assertNull(responseDTO);
    }
}