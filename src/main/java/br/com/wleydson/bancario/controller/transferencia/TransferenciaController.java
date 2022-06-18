package br.com.wleydson.bancario.controller.transferencia;

import br.com.wleydson.bancario.controller.transferencia.assemblable.TransferenciaAssemblable;
import br.com.wleydson.bancario.controller.transferencia.disassembler.TransferenciaDisassembler;
import br.com.wleydson.bancario.controller.transferencia.dto.TransferenciaDTO;
import br.com.wleydson.bancario.controller.transferencia.input.TransferenciaInputDTO;
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
@RequestMapping("transferencia")
public class TransferenciaController {

    private final ContaService contaService;
    private final TransferenciaService transferenciaService;

    private final TransferenciaAssemblable transferenciaAssemblable;

    private final TransferenciaDisassembler transferenciaDisassembler;

    @Operation(summary = "transferencia.contas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "204.padrao"),
            @ApiResponse(responseCode = "400", description = "400.padrao"),
            @ApiResponse(responseCode = "404", description = "conta.nao.encontrada")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferenciaEntreContas(@RequestBody @Valid TransferenciaInputDTO transferenciaInputDTO){
        Conta conta = contaService.buscarConta(transferenciaInputDTO.getContaId());
        Conta contaDestino = contaService.buscarContaAtivaPorNumero(transferenciaInputDTO.getContaDestino());
        transferenciaService.transferir(conta, contaDestino, transferenciaDisassembler.toEntity(transferenciaInputDTO));
    }

    @Operation(summary = "transferencia.reembolsar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "204.padrao"),
            @ApiResponse(responseCode = "400", description = "400.padrao"),
            @ApiResponse(responseCode = "404", description = "transferencia.nao.encontrada")
    })
    @PostMapping("/{id}/reembolsar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferenciaEntreContas(@PathVariable Long id){
        transferenciaService.cancelarTransferenciaPorId(id);
    }


    @Operation(summary = "buscar.transferencia.id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "200.padrao"),
            @ApiResponse(responseCode = "404", description = "transferencia.nao.encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<TransferenciaDTO>> buscarTransferenciaPorId(@PathVariable Long id){
        Transferencia transferencia = transferenciaService.buscarTransferenciaPorId(id);
        return new ResponseEntity(transferenciaAssemblable.toDTO(transferencia), HttpStatus.OK);
    }
}
