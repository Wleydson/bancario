package br.com.wleydson.bancario.controller.conta.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ParcelaDTO {

    private Long id;

    private BigDecimal valor;

    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate dataAgendada;

    @JsonFormat(pattern="dd/MM/yyyy - HH:mm:ss")
    private LocalDateTime dataTransacao;

    private String status;

}
