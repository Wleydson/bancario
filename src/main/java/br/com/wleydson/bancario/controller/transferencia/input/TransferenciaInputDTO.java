package br.com.wleydson.bancario.controller.transferencia.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransferenciaInputDTO {

    @NotNull
    public String contaDestino;

    @NotNull
    public Long contaId;

    @NotNull
    public BigDecimal valor;

    public Integer parcelas;

    public LocalDate dataAgendada;
}
