package br.com.wleydson.bancario.controller.transferencia.assemblable;

import br.com.wleydson.bancario.controller.conta.assemblable.ParcelaAssemblable;
import br.com.wleydson.bancario.controller.conta.dto.ParcelaDTO;
import br.com.wleydson.bancario.controller.transferencia.dto.TransferenciaDTO;
import br.com.wleydson.bancario.enumerations.StatusParcelaEnum;
import br.com.wleydson.bancario.model.Parcela;
import br.com.wleydson.bancario.model.Transferencia;
import br.com.wleydson.bancario.util.Assemblable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransferenciaAssemblable implements Assemblable<Transferencia, TransferenciaDTO> {

    private final ParcelaAssemblable parcelaAssemblable;

    @Override
    public TransferenciaDTO toDTO(Transferencia entity) {
        TransferenciaDTO dto = new TransferenciaDTO();
        dto.setId(entity.getId());
        dto.setNumeroTransacao(entity.getNumeroTransacao());
        dto.setConta(entity.getConta().getConta());
        dto.setContaDestino(entity.getContaDestino());
        dto.setValorTotal(getValorTotal(entity.getParcelas()));
        dto.setValorPago(getValorPago(entity.getParcelas()));
        dto.setDataCriacao(entity.getDataCriacao());
        dto.setDataAtualizacao(entity.getDataAtualizacao());
        dto.setStatus(entity.getStatus().toString());
        dto.setParcelas(getParcelasDTOs(entity.getParcelas()));
        return dto;
    }

    private List<ParcelaDTO> getParcelasDTOs(List<Parcela> parcelas){
        return parcelaAssemblable.toDTOList(parcelas);
    }

    private BigDecimal getValorTotal(List<Parcela> parcelas){
        return parcelas.stream().map(Parcela::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getValorPago(List<Parcela> parcelas){
        return parcelas.stream().filter(parcela -> parcela.getStatus().equals(StatusParcelaEnum.EFETUADA)).map(Parcela::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
