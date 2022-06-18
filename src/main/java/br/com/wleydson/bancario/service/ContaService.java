package br.com.wleydson.bancario.service;

import br.com.wleydson.bancario.config.MessageHelper;
import br.com.wleydson.bancario.exception.ResourceNotFoundException;
import br.com.wleydson.bancario.model.Conta;
import br.com.wleydson.bancario.repository.ContaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ContaService {

    private final ContaRepository contaRepository;
    private final MessageHelper messageHelper;

    public Conta buscarContaAtiva(Long id){
        return contaRepository.findByIdAndAtivoTrue(id).orElseThrow(() -> new ResourceNotFoundException(messageHelper.getMensagem("conta.nao.encontrada")));
    }

    public Conta buscarConta(Long id){
        return contaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(messageHelper.getMensagem("conta.nao.encontrada")));
    }

    public Conta buscarContaAtivaPorNumero(String conta){
        return contaRepository.findByContaAndAtivoTrue(conta).orElseThrow(() -> new ResourceNotFoundException(messageHelper.getMensagem("conta.nao.encontrada")));
    }

    public List<Conta> buscarTodasContasAtivas(){
        return contaRepository.findByAtivoTrue();
    }

    @Transactional
    public void ativarConta(Long id){
        Conta conta = buscarConta(id);
        conta.ativar();
        contaRepository.save(conta);
    }

    @Transactional
    public void desativarConta(Long id){
        Conta conta = buscarConta(id);
        conta.desativar();
        contaRepository.save(conta);
    }

    @Transactional
    public Conta salvarConta(Conta conta) {
        return contaRepository.save(conta);
    }

    @Transactional
    public void depositar(Long id, BigDecimal valor) {
        Conta conta = buscarContaAtiva(id);
        conta.addSaldo(valor);
        contaRepository.save(conta);
    }
}
