package br.com.wleydson.bancario.controller.transferencia.dto;

import br.com.wleydson.bancario.controller.conta.dto.ParcelaDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TransferenciaDTO {

    private Long id;

    private String numeroTransacao;

    private String conta;

    private String contaDestino;

    private BigDecimal valorTotal;

    private BigDecimal valorPago;

    @JsonFormat(pattern="dd/MM/yyyy - HH:mm:ss")
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern="dd/MM/yyyy - HH:mm:ss")
    private LocalDateTime dataAtualizacao;

    private String status;

    private List<ParcelaDTO> parcelas;

}
