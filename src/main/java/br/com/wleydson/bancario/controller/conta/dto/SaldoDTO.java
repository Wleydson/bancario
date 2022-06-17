package br.com.wleydson.bancario.controller.conta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class SaldoDTO {

    private String conta;
    private BigDecimal saldo;
}
