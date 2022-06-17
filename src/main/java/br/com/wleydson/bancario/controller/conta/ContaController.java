package br.com.wleydson.bancario.controller.conta;

import br.com.wleydson.bancario.controller.conta.assemblable.SaldoAssemblable;
import br.com.wleydson.bancario.controller.conta.assemblable.ContaAssemblable;
import br.com.wleydson.bancario.controller.conta.disassembler.TransferenciaDisassembler;
import br.com.wleydson.bancario.controller.conta.dto.ContaDTO;
import br.com.wleydson.bancario.controller.conta.disassembler.ContaDisassembler;
import br.com.wleydson.bancario.controller.conta.dto.SaldoDTO;
import br.com.wleydson.bancario.controller.conta.input.ContaInputDTO;
import br.com.wleydson.bancario.controller.conta.input.DepositoInputDTO;
import br.com.wleydson.bancario.controller.conta.input.TransferenciaInputDTO;
import br.com.wleydson.bancario.model.Conta;
import br.com.wleydson.bancario.service.ContaService;
import br.com.wleydson.bancario.service.TransferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("contas")
public class ContaController {

    private final ContaService contaService;
    private final TransferenciaService transferenciaService;

    private final ContaAssemblable contaAssemblable;
    private final SaldoAssemblable saldoAssemblable;

    private final ContaDisassembler contaDisassembler;
    private final TransferenciaDisassembler transferenciaDisassembler;

    @GetMapping("/{id}/saldo")
    public ResponseEntity<SaldoDTO> buscarSaldo(@PathVariable Long id){
        Conta conta = contaService.buscarContaAtiva(id);
        return new ResponseEntity(saldoAssemblable.toDTO(conta), HttpStatus.OK);
    }

    @PostMapping("/{id}/transferir")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferenciaEntreContas(@PathVariable Long id, @RequestBody @Valid TransferenciaInputDTO transferenciaInputDTO){
        transferenciaService.transferir(id, transferenciaDisassembler.toEntity(transferenciaInputDTO));
    }

    @GetMapping
    public ResponseEntity<List<ContaDTO>> buscarContas(){
        List<Conta> contas = contaService.buscarTodasContasAtivas();
        return new ResponseEntity(contaAssemblable.toDTOList(contas), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaDTO> buscarPorId(@PathVariable Long id){
        Conta conta = contaService.buscarContaAtiva(id);
        return new ResponseEntity(contaAssemblable.toDTO(conta), HttpStatus.OK);
    }

    @PatchMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarConta(@PathVariable Long id){
        contaService.ativarConta(id);
    }

    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarConta(@PathVariable Long id){
        contaService.desativarConta(id);
    }

    @PostMapping
    public ResponseEntity<List<ContaDTO>> inserirConta(@RequestBody @Valid ContaInputDTO contaInput){
        Conta conta = contaService.salvarConta(contaDisassembler.toEntity(contaInput));
        return new ResponseEntity(contaAssemblable.toDTO(conta), HttpStatus.OK);
    }

    @PostMapping("/{id}/depositar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void depositar(@PathVariable Long id, @RequestBody @Valid DepositoInputDTO depositoInputDTO){
        contaService.depositar(id, depositoInputDTO.getValor());
    }
}
