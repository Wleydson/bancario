package br.com.wleydson.bancario.controller;

import br.com.wleydson.bancario.controller.conta.dto.ContaDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContaControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    @DisplayName("Deve buscar todas as contas ativas")
    public void buscarTodasContasComSucesso() throws Exception {
        ResponseEntity<ContaDTO[]> response = this.testRestTemplate.exchange("/contas", HttpMethod.GET, null, ContaDTO[].class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().length, 2);
    }

    @Test
    @Order(2)
    @DisplayName("Deve buscar conta por id com sucesso")
    public void buscarContaPorIdComSucesso(){
        ResponseEntity<ContaDTO> response = this.testRestTemplate.exchange("/contas/".concat("1"), HttpMethod.GET, null, ContaDTO.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getId(), 1L);
        assertEquals(response.getBody().getConta(), "57891");
    }

    @Test
    @Order(3)
    @DisplayName("Deve retorna um erro com id nao encontrado")
    public void naoDeveBuscarConta(){
        ResponseEntity<ContaDTO> response = this.testRestTemplate.exchange("/contas/".concat("10"), HttpMethod.GET, null, ContaDTO.class);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(4)
    @DisplayName("Deve ativar conta com sucesso")
    public void deveAtivarConta(){
        ResponseEntity<Object> response = this.testRestTemplate.exchange("/contas/".concat("3").concat("/ativar"), HttpMethod.PATCH, null, Object.class);

        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);

        ResponseEntity<ContaDTO> r = this.testRestTemplate.exchange("/contas/".concat("1"), HttpMethod.GET, null, ContaDTO.class);

        assertEquals(r.getStatusCode(), HttpStatus.OK);
        assertEquals(r.getBody().getId(), 3L);
    }
}
