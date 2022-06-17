package br.com.wleydson.bancario.controller.conta.assemblable;

import br.com.wleydson.bancario.controller.conta.dto.ContaDTO;
import br.com.wleydson.bancario.model.Conta;
import br.com.wleydson.bancario.util.Assemblable;
import org.springframework.stereotype.Component;

@Component
public class ContaAssemblable implements Assemblable<Conta, ContaDTO> {

    @Override
    public ContaDTO toDTO(Conta entity) {
        ContaDTO dto = new ContaDTO();
        dto.setId(entity.getId());
        dto.setAgencia(entity.getAgencia());
        dto.setConta(entity.getConta());
        dto.setDataCriacao(entity.getDataCriacao());
        return dto;
    }
}
