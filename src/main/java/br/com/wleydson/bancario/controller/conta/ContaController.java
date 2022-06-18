package br.com.wleydson.bancario.controller.conta;

import br.com.wleydson.bancario.controller.conta.assemblable.SaldoAssemblable;
import br.com.wleydson.bancario.controller.conta.assemblable.ContaAssemblable;
import br.com.wleydson.bancario.controller.transferencia.assemblable.TransferenciaAssemblable;
import br.com.wleydson.bancario.controller.transferencia.disassembler.TransferenciaDisassembler;
import br.com.wleydson.bancario.controller.conta.dto.ContaDTO;
import br.com.wleydson.bancario.controller.conta.disassembler.ContaDisassembler;
import br.com.wleydson.bancario.controller.conta.dto.SaldoDTO;
import br.com.wleydson.bancario.controller.transferencia.dto.TransferenciaDTO;
import br.com.wleydson.bancario.controller.conta.input.ContaInputDTO;
import br.com.wleydson.bancario.controller.conta.input.DepositoInputDTO;
import br.com.wleydson.bancario.model.Conta;
import br.com.wleydson.bancario.model.Transferencia;
import br.com.wleydson.bancario.service.ContaService;
import br.com.wleydson.bancario.service.TransferenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final TransferenciaAssemblable transferenciaAssemblable;

    private final ContaDisassembler contaDisassembler;


    @Operation(summary = "conta.saldo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "200.padrao"),
            @ApiResponse(responseCode = "400", description = "400.padrao"),
            @ApiResponse(responseCode = "404", description = "conta.nao.encontrada")
    })
    @GetMapping("/{id}/saldo")
    public ResponseEntity<SaldoDTO> buscarSaldo(@PathVariable Long id){
        Conta conta = contaService.buscarContaAtiva(id);
        return new ResponseEntity(saldoAssemblable.toDTO(conta), HttpStatus.OK);
    }


    @Operation(summary = "conta.transferencias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "200.padrao"),
            @ApiResponse(responseCode = "400", description = "400.padrao"),
            @ApiResponse(responseCode = "404", description = "conta.nao.encontrada")
    })
    @GetMapping("/{id}/transferencia")
    public ResponseEntity<List<TransferenciaDTO>> buscarTransferenciaPorConta(@PathVariable Long id){
        Conta conta = contaService.buscarContaAtiva(id);
        List<Transferencia> transferencias = transferenciaService.buscarTransferencias(conta);
        return new ResponseEntity(transferenciaAssemblable.toDTOList(transferencias), HttpStatus.OK);
    }


    @Operation(summary = "conta.todas.ativas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "200.padrao")
    })
    @GetMapping
    public ResponseEntity<List<ContaDTO>> buscarContas(){
        List<Conta> contas = contaService.buscarTodasContasAtivas();
        return new ResponseEntity(contaAssemblable.toDTOList(contas), HttpStatus.OK);
    }


    @Operation(summary = "conta.id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "200.padrao"),
            @ApiResponse(responseCode = "404", description = "conta.nao.encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContaDTO> buscarPorId(@PathVariable Long id){
        Conta conta = contaService.buscarContaAtiva(id);
        return new ResponseEntity(contaAssemblable.toDTO(conta), HttpStatus.OK);
    }


    @Operation(summary = "conta.ativar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "204.padrao"),
            @ApiResponse(responseCode = "404", description = "conta.nao.encontrada")
    })
    @PatchMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarConta(@PathVariable Long id){
        contaService.ativarConta(id);
    }


    @Operation(summary = "conta.desativar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "204.padrao"),
            @ApiResponse(responseCode = "404", description = "conta.nao.encontrada")
    })
    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarConta(@PathVariable Long id){
        contaService.desativarConta(id);
    }


    @Operation(summary = "conta.salvar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "200.padrao"),
            @ApiResponse(responseCode = "400", description = "400.padrao")
    })
    @PostMapping
    public ResponseEntity<List<ContaDTO>> inserirConta(@RequestBody @Valid ContaInputDTO contaInput){
        Conta conta = contaService.salvarConta(contaDisassembler.toEntity(contaInput));
        return new ResponseEntity(contaAssemblable.toDTO(conta), HttpStatus.OK);
    }


    @Operation(summary = "conta.depositar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "204.padrao"),
            @ApiResponse(responseCode = "404", description = "conta.nao.encontrada")
    })
    @PatchMapping("/{id}/depositar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void depositar(@PathVariable Long id, @RequestBody @Valid DepositoInputDTO depositoInputDTO){
        contaService.depositar(id, depositoInputDTO.getValor());
    }
}
