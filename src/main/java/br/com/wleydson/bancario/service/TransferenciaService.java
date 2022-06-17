package br.com.wleydson.bancario.service;

import br.com.wleydson.bancario.config.MessageHelper;
import br.com.wleydson.bancario.enumerations.StatusParcelaEnum;
import br.com.wleydson.bancario.enumerations.StatusTransferenciaEnum;
import br.com.wleydson.bancario.exception.TransferenciaException;
import br.com.wleydson.bancario.model.Conta;
import br.com.wleydson.bancario.model.Parcela;
import br.com.wleydson.bancario.model.Transferencia;
import br.com.wleydson.bancario.repository.ParcelaRepository;
import br.com.wleydson.bancario.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final ParcelaRepository parcelaRepository;
    private final ContaService contaService;
    private final MessageHelper messageHelper;

    @Transactional
    public void transferir(Long id, Transferencia transferencia) {
        Conta conta = contaService.buscarContaAtiva(id);

        transferencia.setNumeroTransacao(UUID.randomUUID().toString());
        transferencia.setConta(conta);
        transferencia.setStatus(getStatus(transferencia));

        realizarTransferencia(transferencia, conta);
        transferenciaRepository.save(transferencia);
        parcelaRepository.saveAll(transferencia.getParcelas());
    }

    private void realizarTransferencia(Transferencia transferencia, Conta conta) {
        BigDecimal valorTransferido = verificarSaldo(transferencia, conta);
        Conta contaDestino = contaService.buscarContaPorNumero(transferencia.getContaDestino());

        conta.removeSaldo(valorTransferido);
        contaService.salvarConta(conta);

        contaDestino.addSaldo(valorTransferido);
        contaService.salvarConta(contaDestino);
    }

    private BigDecimal verificarSaldo(Transferencia transferencia, Conta conta) {
        BigDecimal valor = transferencia.getParcelas()
                                                        .stream()
                                                        .filter(parcela -> parcela.getStatus().equals(StatusParcelaEnum.EFETUADA))
                                                        .map(Parcela::getValor)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        if(conta.getSaldo().compareTo(valor) == -1){
            throw new TransferenciaException(messageHelper.getMensagem("saldo.insuficiente"));
        }

        return valor;
    }

    private StatusTransferenciaEnum getStatus(Transferencia transferencia){
        if(transferencia.getParcelas().stream().filter(p -> p.getStatus().equals(StatusParcelaEnum.AGENDADA)).count() > 0){
            return StatusTransferenciaEnum.ABERTO;
        }
        return StatusTransferenciaEnum.FINALIZADO;
    }
}
