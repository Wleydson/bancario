package br.com.wleydson.bancario.controller.conta.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ContaInputDTO {

    @NotBlank(message = "{conta.obrigatorio}")
    private String conta;

}
