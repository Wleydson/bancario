package br.com.wleydson.bancario.controller.conta.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class DepositoInputDTO {

    @NotNull(message = "{valor.obrigatorio}")
    private BigDecimal valor;

}
