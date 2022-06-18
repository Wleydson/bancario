package br.com.wleydson.bancario.controller;

import br.com.wleydson.bancario.controller.conta.dto.SaldoDTO;
import br.com.wleydson.bancario.controller.transferencia.dto.TransferenciaDTO;
import br.com.wleydson.bancario.controller.transferencia.input.TransferenciaInputDTO;
import br.com.wleydson.bancario.enumerations.StatusTransferenciaEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransferenciaControllerTest extends AbstractControllerTest{

    private void verificarSaldoConta(Long id, BigDecimal valor) throws Exception {
        mockMvc.perform(get("/contas/"+id+"/saldo"))
                .andDo(payloadExtractor)
                .andExpect(status().isOk())
                .andReturn();

        SaldoDTO conta = payloadExtractor.as(SaldoDTO.class);

        assertEquals(conta.getSaldo(), valor);
    }

    @Test
    @Order(1)
    @DisplayName("Deve realizar transferencia entre contas com sucesso")
    public void semAgendamentoSemParcelaTest() throws Exception {
        verificarSaldoConta(1L,  new BigDecimal(1000));
        verificarSaldoConta(2L,  new BigDecimal(5000));

        TransferenciaInputDTO dto = new TransferenciaInputDTO();
        dto.setContaDestino("746183");
        dto.setContaId(1L);
        dto.setValor(new BigDecimal(200));

        mockMvc.perform(post("/transferencia")
                .content(json(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isNoContent())
                .andReturn();

        verificarSaldoConta(1L,  new BigDecimal(800));
        verificarSaldoConta(2L,  new BigDecimal(5200));

    }

    @Test
    @Order(2)
    @DisplayName("Deve retorna erro ao tenta transferir, conta retirada nao existe")
    public void semDataSemParcelaErroContaRetiradaTest() throws Exception {
        verificarSaldoConta(1L,  new BigDecimal(800));
        verificarSaldoConta(2L,  new BigDecimal(5200));

        TransferenciaInputDTO dto = new TransferenciaInputDTO();
        dto.setContaDestino("746183");
        dto.setContaId(197L);
        dto.setValor(new BigDecimal(200));

        mockMvc.perform(post("/transferencia")
                .content(json(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isNotFound())
                .andReturn();

        verificarSaldoConta(2L,  new BigDecimal(5200));
    }

    @Test
    @Order(3)
    @DisplayName("Deve retorna erro ao tenta transferir, conta deposito nao existe")
    public void semDataSemParcelaErroContaDepositoTest() throws Exception {
        verificarSaldoConta(1L,  new BigDecimal(800));
        verificarSaldoConta(2L,  new BigDecimal(5200));

        TransferenciaInputDTO dto = new TransferenciaInputDTO();
        dto.setContaDestino("987123");
        dto.setContaId(1L);
        dto.setValor(new BigDecimal(200));

        mockMvc.perform(post("/transferencia")
                .content(json(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isNotFound())
                .andReturn();

        verificarSaldoConta(1L,  new BigDecimal(800));
    }

    @Test
    @Order(4)
    @DisplayName("Deve retorna uma transferencia")
    public void buscarPorIdTest() throws Exception {

        mockMvc.perform(get("/transferencia/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isOk())
                .andReturn();

        TransferenciaDTO dto = payloadExtractor.as(TransferenciaDTO.class);

        assertEquals(dto.getId(), 1L);
        assertEquals(dto.getValorPago(), new BigDecimal(200));
        assertEquals(dto.getValorTotal(), new BigDecimal(200));
        assertEquals(dto.getConta(), "57891");
        assertEquals(dto.getContaDestino(), "746183");
        assertEquals(dto.getStatus(), StatusTransferenciaEnum.FINALIZADO.toString());
        assertEquals(dto.getParcelas().size(), 1);
        assertEquals(dto.getDataCriacao().toLocalDate(), LocalDate.now());
        assertEquals(dto.getDataAtualizacao().toLocalDate(), LocalDate.now());
        assertNotNull(dto.getNumeroTransacao());
    }

    @Test
    @Order(5)
    @DisplayName("Deve retorna um erro ao buscar transferencia que nao existe")
    public void buscarPorIdErroTest() throws Exception {

        mockMvc.perform(get("/transferencia/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Order(6)
    @DisplayName("Deve realizar transferencia parcelada entre contas com sucesso")
    public void semAgendamentoComParcelaTest() throws Exception {
        verificarSaldoConta(1L,  new BigDecimal(800));
        verificarSaldoConta(2L,  new BigDecimal(5200));

        TransferenciaInputDTO dto = new TransferenciaInputDTO();
        dto.setContaDestino("746183");
        dto.setContaId(1L);
        dto.setValor(new BigDecimal(200));
        dto.setParcelas(4);

        mockMvc.perform(post("/transferencia")
                .content(json(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isNoContent())
                .andReturn();

        verificarSaldoConta(1L,  new BigDecimal(800));
        verificarSaldoConta(2L,  new BigDecimal(5200));

        mockMvc.perform(get("/transferencia/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isOk())
                .andReturn();

        TransferenciaDTO transferenciaDTO = payloadExtractor.as(TransferenciaDTO.class);
        assertEquals(transferenciaDTO.getStatus(), StatusTransferenciaEnum.ABERTO.toString());
        assertEquals(transferenciaDTO.getParcelas().size(), 4);
        assertEquals(transferenciaDTO.getParcelas().stream().findAny().get().getValor(), new BigDecimal(50));
        assertEquals(transferenciaDTO.getParcelas().stream().findAny().get().getDataAgendada(), LocalDate.now().plusMonths(1));

    }

    @Test
    @Order(7)
    @DisplayName("Deve realizar transferencia parcelada e com data de agendamento entre contas com sucesso")
    public void comAgendamentoComParcelaTest() throws Exception {
        verificarSaldoConta(1L,  new BigDecimal(800));
        verificarSaldoConta(2L,  new BigDecimal(5200));

        TransferenciaInputDTO dto = new TransferenciaInputDTO();
        dto.setContaDestino("746183");
        dto.setContaId(1L);
        dto.setValor(new BigDecimal(200));
        dto.setParcelas(4);
        dto.setDataAgendada(LocalDate.now().plusMonths(5));

        mockMvc.perform(post("/transferencia")
                .content(json(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isNoContent())
                .andReturn();

        verificarSaldoConta(1L,  new BigDecimal(800));
        verificarSaldoConta(2L,  new BigDecimal(5200));

        mockMvc.perform(get("/transferencia/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isOk())
                .andReturn();

        TransferenciaDTO transferenciaDTO = payloadExtractor.as(TransferenciaDTO.class);
        assertEquals(transferenciaDTO.getStatus(), StatusTransferenciaEnum.ABERTO.toString());
        assertEquals(transferenciaDTO.getParcelas().size(), 4);
        assertEquals(transferenciaDTO.getParcelas().stream().findAny().get().getValor(), new BigDecimal(50));
        assertEquals(transferenciaDTO.getParcelas().stream().findAny().get().getDataAgendada(), LocalDate.now().plusMonths(5));

    }

    @Test
    @Order(8)
    @DisplayName("Nao deve realizar transferencia parcelada e com data de agendamento entre contas")
    public void comAgendamentoComParcelaErroTest() throws Exception {
        verificarSaldoConta(1L,  new BigDecimal(800));
        verificarSaldoConta(2L,  new BigDecimal(5200));

        TransferenciaInputDTO dto = new TransferenciaInputDTO();
        dto.setContaDestino("746183");
        dto.setContaId(1L);
        dto.setValor(new BigDecimal(200));
        dto.setParcelas(4);
        dto.setDataAgendada(LocalDate.now().minusMonths(5));

        mockMvc.perform(post("/transferencia")
                .content(json(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(payloadExtractor)
                .andExpect(status().isBadRequest())
                .andReturn();


    }

}
