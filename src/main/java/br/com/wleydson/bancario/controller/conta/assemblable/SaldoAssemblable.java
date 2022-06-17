package br.com.wleydson.bancario.controller.conta.assemblable;

import br.com.wleydson.bancario.controller.conta.dto.SaldoDTO;
import br.com.wleydson.bancario.model.Conta;
import br.com.wleydson.bancario.util.Assemblable;
import org.springframework.stereotype.Component;

@Component
public class SaldoAssemblable implements Assemblable<Conta, SaldoDTO> {

    @Override
    public SaldoDTO toDTO(Conta entity) {
        SaldoDTO dto = new SaldoDTO();
        dto.setConta(entity.getConta());
        dto.setSaldo(entity.getSaldo());
        return dto;
    }
}
