package br.com.wleydson.bancario.controller.conta.assemblable;

import br.com.wleydson.bancario.controller.conta.dto.ParcelaDTO;
import br.com.wleydson.bancario.model.Parcela;
import br.com.wleydson.bancario.util.Assemblable;
import org.springframework.stereotype.Component;

@Component
public class ParcelaAssemblable implements Assemblable<Parcela, ParcelaDTO> {

    @Override
    public ParcelaDTO toDTO(Parcela entity) {
        ParcelaDTO dto = new ParcelaDTO();
        dto.setId(entity.getId());
        dto.setValor(entity.getValor());
        dto.setDataAgendada(entity.getDataAgendada());
        dto.setDataTransacao(entity.getDataTransacao());
        dto.setStatus(entity.getStatus().toString());
        return dto;
    }
}
