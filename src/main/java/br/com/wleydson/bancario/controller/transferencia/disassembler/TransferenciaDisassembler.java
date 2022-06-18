package br.com.wleydson.bancario.controller.transferencia.disassembler;

import br.com.wleydson.bancario.config.MessageHelper;
import br.com.wleydson.bancario.controller.transferencia.input.TransferenciaInputDTO;
import br.com.wleydson.bancario.enumerations.StatusParcelaEnum;
import br.com.wleydson.bancario.exception.TransferenciaException;
import br.com.wleydson.bancario.model.Parcela;
import br.com.wleydson.bancario.model.Transferencia;
import br.com.wleydson.bancario.util.Disassemblable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class TransferenciaDisassembler implements Disassemblable<TransferenciaInputDTO, Transferencia> {

    private final MessageHelper messageHelper;

    @Override
    public Transferencia toEntity(TransferenciaInputDTO input) {
        validarDataAgendada(input);
        Transferencia transferencia = new Transferencia();
        transferencia.setContaDestino(input.getContaDestino());
        transferencia.setParcelas(getParcelas(input, transferencia));

        return transferencia;
    }

    private List<Parcela> getParcelas(TransferenciaInputDTO input, Transferencia transferencia) {
        int numeroParcelas = 1;
        if(Objects.nonNull(input.getParcelas())){
            numeroParcelas = input.getParcelas();
        }

        List<Parcela> parcelas = new ArrayList<>();
        BigDecimal valorPorParcela = input.getValor().divide(new BigDecimal(numeroParcelas));
        for (int i = 1; i <= numeroParcelas; i++) {
           parcelas.add(montarParcela(input.getDataAgendada(), transferencia, valorPorParcela, i, numeroParcelas));
        }

        return parcelas;
    }

    private Parcela montarParcela(LocalDate dataAgendada, Transferencia transferencia, BigDecimal valorPorParcela, Integer numeroParcela, Integer numeroTotalParcelas) {
        StatusParcelaEnum status = numeroTotalParcelas > 1 ? StatusParcelaEnum.AGENDADA : StatusParcelaEnum.EFETUADA;

        Parcela parcela = new Parcela(transferencia);
        parcela.setValor(valorPorParcela);
        parcela.setStatus(status);
        parcela.setDataAgendada(getDataAgendamento(dataAgendada, numeroParcela, status));
        if(parcela.getStatus().equals(StatusParcelaEnum.EFETUADA)){
            parcela.setDataTransacao(LocalDateTime.now());
        }
        return parcela;
    }

    private LocalDate getDataAgendamento(LocalDate dataAgendada, Integer numeroParcela, StatusParcelaEnum status) {
        LocalDate data = Objects.nonNull(dataAgendada) ? dataAgendada.minusMonths(1) : LocalDate.now();
        if(status.equals(StatusParcelaEnum.AGENDADA)){
            data = data.plusMonths(numeroParcela);
        }
        return data;
    }

    private void validarDataAgendada(TransferenciaInputDTO input) {
        if(Objects.nonNull(input.getDataAgendada()) && input.getDataAgendada().isAfter(LocalDate.now())){
            throw new TransferenciaException(messageHelper.getMensagem("data.agendada.antiga"));
        }
    }
}
