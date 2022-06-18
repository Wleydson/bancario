package br.com.wleydson.bancario.service;

import br.com.wleydson.bancario.config.MessageHelper;
import br.com.wleydson.bancario.enumerations.StatusParcelaEnum;
import br.com.wleydson.bancario.enumerations.StatusTransferenciaEnum;
import br.com.wleydson.bancario.exception.ResourceNotFoundException;
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
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final ParcelaRepository parcelaRepository;
    private final ContaService contaService;
    private final MessageHelper messageHelper;


    public List<Transferencia> buscarTransferencias(Conta conta){
        return transferenciaRepository.findByConta(conta);
    }

    @Transactional
    public void cancelarTransferenciaPorId(Long id) {
        Transferencia transferencia = buscarTransferenciaPorId(id);

        Conta contaRetirada = contaService.buscarContaAtivaPorNumero(transferencia.getContaDestino());
        Conta contaDestino = transferencia.getConta();
        BigDecimal valorDevolvido = getValorTransferencia(transferencia);

        realizarTransferencia(contaRetirada, contaDestino, valorDevolvido);

        cancelarTransferencia(transferencia);
        salvarTransferencia(transferencia);
    }

    @Transactional
    public void transferir(Conta conta, Conta contaDestino, Transferencia transferencia) {
        transferencia.setNumeroTransacao(UUID.randomUUID().toString());
        transferencia.setConta(conta);
        transferencia.setStatus(getStatus(transferencia));

        BigDecimal valorTransferido = verificarSaldo(transferencia, conta);
        realizarTransferencia(conta, contaDestino, valorTransferido);

        salvarTransferencia(transferencia);
    }

    private void realizarTransferencia(Conta contaRetirada, Conta contaDestino, BigDecimal valorTransferido) {
        contaRetirada.subSaldo(valorTransferido);
        contaService.salvarConta(contaRetirada);

        contaDestino.addSaldo(valorTransferido);
        contaService.salvarConta(contaDestino);
    }

    private void salvarTransferencia(Transferencia transferencia) {
        transferenciaRepository.save(transferencia);
        parcelaRepository.saveAll(transferencia.getParcelas());
    }

    private void cancelarTransferencia(Transferencia transferencia) {
        transferencia.setStatus(StatusTransferenciaEnum.CANCELADO);
        transferencia.getParcelas().forEach(p -> p.setStatus(StatusParcelaEnum.CANCELADA));
    }

    private BigDecimal verificarSaldo(Transferencia transferencia, Conta conta) {
        BigDecimal valor = getValorTransferencia(transferencia);

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

    public Transferencia buscarTransferenciaPorId(Long id){
        return transferenciaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(messageHelper.getMensagem("transferencia.nao.encontrada")));
    }

    private BigDecimal getValorTransferencia(Transferencia transferencia) {
        return transferencia.getParcelas()
                .stream()
                .filter(p -> p.getStatus().equals(StatusParcelaEnum.EFETUADA))
                .map(Parcela::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
