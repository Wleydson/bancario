package br.com.wleydson.bancario.controller;

import br.com.wleydson.bancario.controller.conta.dto.ContaDTO;
import br.com.wleydson.bancario.controller.conta.dto.SaldoDTO;
import br.com.wleydson.bancario.controller.conta.input.ContaInputDTO;
import br.com.wleydson.bancario.controller.conta.input.DepositoInputDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContaControllerTest extends AbstractControllerTest{

    @Test
    @Order(1)
    @DisplayName("Deve buscar todas as contas ativas")
    public void buscarTodasContasComSucessoTest() throws Exception {
        mockMvc.perform(get("/contas"))
                .andDo(payloadExtractor)
                .andExpect(status().isOk())
                .andReturn();

        List<ContaDTO> contas = payloadExtractor.asListOf(ContaDTO.class);
        assertEquals(contas.size(), 2);
    }

    @Test
    @Order(2)
    @DisplayName("Deve buscar conta por id com sucesso")
    public void buscarContaPorIdComSucessoTest() throws Exception {
        mockMvc.perform(get("/contas/1"))
                .andDo(payloadExtractor)
                .andExpect(status().isOk())
                .andReturn();

        ContaDTO conta = payloadExtractor.as(ContaDTO.class);

        assertEquals(conta.getId(), 1L);
        assertEquals(conta.getConta(), "57891");
    }

    @Test
    @Order(3)
    @DisplayName("Deve retorna um erro com id nao encontrado")
    public void naoDeveBuscarContaTest() throws Exception {
        mockMvc.perform(get("/contas/10"))
                .andDo(payloadExtractor)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(4)
    @DisplayName("Deve ativar conta com sucesso")
    public void deveAtivarContaTest() throws Exception {
        mockMvc.perform(patch("/contas/3/ativar"))
                .andDo(payloadExtractor)
                .andExpect(status().isNoContent())
                .andReturn();

        mockMvc.perform(get("/contas/3"))
                .andDo(payloadExtractor)
                .andExpect(status().isOk())
                .andReturn();

        ContaDTO conta = payloadExtractor.as(ContaDTO.class);

        assertEquals(conta.getConta(),"85367");
    }

    @Test
    @Order(5)
    @DisplayName("Deve retorna um erro ao ativar conta que nao existe")
    public void erroAtivarContaTest() throws Exception {
        mockMvc.perform(patch("/contas/10/ativar"))
                .andDo(payloadExtractor)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(6)
    @DisplayName("Deve desativar conta com sucesso")
    public void deveDesativarContaTest() throws Exception {
        mockMvc.perform(patch("/contas/2/desativar"))
                .andDo(payloadExtractor)
                .andExpect(status().isNoContent())
                .andReturn();

        mockMvc.perform(get("/contas/2"))
                .andDo(payloadExtractor)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(7)
    @DisplayName("Deve retorna um erro ao desativar conta que nao existe")
    public void erroDesativarContaTest() throws Exception {
        mockMvc.perform(patch("/contas/10/desativar"))
                .andDo(payloadExtractor)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(8)
    @DisplayName("Deve buscar saldo com sucesso")
    public void saldoComSucessoTest() throws Exception {
        mockMvc.perform(get("/contas/1/saldo"))
                .andDo(payloadExtractor)
                .andExpect(status().isOk())
                .andReturn();

        SaldoDTO conta = payloadExtractor.as(SaldoDTO.class);

        assertEquals(conta.getSaldo(), new BigDecimal(1000));
        assertEquals(conta.getConta(), "57891");
    }

    @Test
    @Order(9)
    @DisplayName("Deve buscar saldo com erro conta nao existe")
    public void saldoComErroTest() throws Exception {
        mockMvc.perform(get("/contas/41/saldo"))
                .andDo(payloadExtractor)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(10)
    @DisplayName("Depositar com sucesso")
    public void depositarComSucessoTest() throws Exception {
        DepositoInputDTO depositoInputDTO = new DepositoInputDTO();
        depositoInputDTO.setValor(new BigDecimal(500));

        mockMvc.perform(patch("/contas/1/depositar")
                .content(json(depositoInputDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isNoContent())
                .andReturn();

        mockMvc.perform(get("/contas/1/saldo"))
                .andDo(payloadExtractor)
                .andExpect(status().isOk())
                .andReturn();

        SaldoDTO conta = payloadExtractor.as(SaldoDTO.class);
        assertEquals(conta.getSaldo(), new BigDecimal(1500));
        assertEquals(conta.getConta(), "57891");
    }

    @Test
    @Order(11)
    @DisplayName("Depositar com erro conta não existe")
    public void depositarComErroTest() throws Exception {
        DepositoInputDTO depositoInputDTO = new DepositoInputDTO();
        depositoInputDTO.setValor(new BigDecimal(500));

        mockMvc.perform(patch("/contas/15/depositar")
                .content(json(depositoInputDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(12)
    @DisplayName("Salvar conta com sucesso")
    public void inserirContaComSucessoTest() throws Exception {
        ContaInputDTO contaInputDTO = new ContaInputDTO();
        contaInputDTO.setConta("123456");

        mockMvc.perform(post("/contas/")
                .content(json(contaInputDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isOk())
                .andReturn();

        ContaDTO conta = payloadExtractor.as(ContaDTO.class);
        assertEquals(conta.getId(), 4);
        assertEquals(conta.getConta(), "123456");
        assertEquals(conta.getDataCriacao().toLocalDate(), LocalDate.now());
    }

    @Test
    @Order(13)
    @DisplayName("Deve buscar transferencias da conta com sucesso")
    public void deveBuscarTransferenciaDaContaComSucessoTest() throws Exception {

        mockMvc.perform(get("/contas/1/transferencia")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(14)
    @DisplayName("Deve retorna um erro ao busca as transferencias")
    public void deveBuscarTransferenciaDaContaComErroTest() throws Exception {

        mockMvc.perform(get("/contas/81/transferencia")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
