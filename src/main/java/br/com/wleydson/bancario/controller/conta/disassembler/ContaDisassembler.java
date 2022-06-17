package br.com.wleydson.bancario.controller.conta.disassembler;

import br.com.wleydson.bancario.controller.conta.input.ContaInputDTO;
import br.com.wleydson.bancario.model.Conta;
import br.com.wleydson.bancario.util.Disassemblable;
import org.springframework.stereotype.Component;

@Component
public class ContaDisassembler implements Disassemblable<ContaInputDTO, Conta> {

    @Override
    public Conta toEntity(ContaInputDTO input) {
        Conta conta = new Conta();
        conta.setConta(input.getConta());
        return conta;
    }
}
