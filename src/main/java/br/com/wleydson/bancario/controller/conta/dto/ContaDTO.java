package br.com.wleydson.bancario.controller.conta.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ContaDTO {

    private Long id;

    private String agencia;

    private String conta;

    @JsonFormat(pattern="dd/MM/yyyy - HH:mm:ss")
    private LocalDateTime dataCriacao;

}
