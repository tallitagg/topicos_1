package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.acme.dto.ProdutoDTO;
import org.acme.dto.ProdutoResponseDTO;
import org.acme.service.ProdutoService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class ProdutoResourceTest {

    @Inject
    ProdutoService produtoService;

    @Test
    void buscarTodos() {
        RestAssured.given()
                .when()
                .get("/produtos")
                .then()
                .statusCode(200);
    }

    @Test
    void buscarPorNome() {
        RestAssured.given()
                .pathParam("nome", "Frost 500 Inox")
                .when()
                .get("/produtos/nome/{nome}")
                .then()
                .statusCode(200)
                .body("nome", CoreMatchers.hasItem("Frost 500 Inox"));
    }

    @Test
    void buscarPorPreco() {
        RestAssured.given()
                .pathParam("preco", 149)
                .when()
                .get("/produtos/preco/{preco}")
                .then()
                .statusCode(200)
                .body("preco", CoreMatchers.hasItem(149));
    }

    @Test
    void buscarPorMarca() {
        RestAssured.given()
                .pathParam("marca", "HydraPro")
                .when()
                .get("/produtos/marca/{marca}")
                .then()
                .statusCode(200)
                .body("marca.nome", CoreMatchers.hasItem("HydraPro"));
    }

    @Test
    void buscarPorModelo() {
        RestAssured.given()
                .pathParam("modelo", "Kids Pop 350")
                .when()
                .get("/produtos/modelo/{modelo}")
                .then()
                .statusCode(200)
                .body("modelo.nome", CoreMatchers.hasItem("Kids Pop 350"));
    }

    @Test
    void buscarPorMaterial() {
        RestAssured.given()
                .pathParam("material", "Aço Inox 304 (corpo externo escovado)")
                .when()
                .get("/produtos/material/{material}")
                .then()
                .statusCode(200)
                .body("material.tipo", CoreMatchers.hasItem("Aço Inox 304 (corpo externo escovado)"));
    }

    @Test
    void buscarPorCapacidade() {
        RestAssured.given()
                .pathParam("capacidade", 0.75)
                .when()
                .get("/produtos/capacidade/{capacidade}")
                .then()
                .statusCode(200)
                .body("capacidade", CoreMatchers.hasItem(0.75F));
    }

    @Test
    void incluirTest() {
        ProdutoDTO dto = new ProdutoDTO(
                "Garrafa Térmica Thermo 500",
                "Garrafa inox com isolamento duplo",
                250L,
                0.5,
                1L,
                1L,
                1L,
                1L,
                1L,
                List.of(1L)
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/produtos")
                .then()
                .statusCode(201)
                .body("nome", CoreMatchers.is("Garrafa Térmica Thermo 500"))
                .body("descricao", CoreMatchers.is("Garrafa inox com isolamento duplo"))
                .body("capacidade", CoreMatchers.is(0.5F))
                .body("marca.id", CoreMatchers.is(1))
                .body("modelo.id", CoreMatchers.is(1));
    }

    @Test
    void atualizarTest() {
        ProdutoDTO dto = new ProdutoDTO(
                "Garrafa Térmica Frost 500",
                "Garrafa inox com tampa plástica",
                200L,
                0.5,
                1L,
                1L,
                1L,
                1L,
                1L,
                List.of(1L)
        );

        ProdutoResponseDTO responseDTO = produtoService.create(dto);

        ProdutoDTO dtoUpdate = new ProdutoDTO(
                "Garrafa Térmica Frost 500 Plus",
                "Nova versão com isolamento reforçado",
                250L,
                0.75,
                1L,
                1L,
                1L,
                1L,
                1L,
                List.of(1L)
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when()
                .put("/produtos/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = produtoService.findById(responseDTO.id());

        assertEquals(dtoUpdate.nome(), responseDTO.nome());
        assertEquals(dtoUpdate.descricao(), responseDTO.descricao());
        assertEquals(dtoUpdate.preco(), responseDTO.preco());
        assertEquals(dtoUpdate.capacidade(), responseDTO.capacidade());
    }

    @Test
    void excluirTest() {
        ProdutoDTO dto = new ProdutoDTO(
                "Garrafa Térmica Steel 750",
                "Garrafa inox de 750ml com tampa de pressão",
                300L,
                0.75,
                1L,
                1L,
                1L,
                1L,
                1L,
                List.of(1L)
        );

        ProdutoResponseDTO responseDTO = produtoService.create(dto);

        RestAssured.given()
                .when()
                .delete("/produtos/" + responseDTO.id())
                .then()
                .statusCode(204);

        responseDTO = produtoService.findById(responseDTO.id());

        assertNull(responseDTO);
    }


}
