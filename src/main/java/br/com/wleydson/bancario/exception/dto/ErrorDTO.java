package br.com.wleydson.bancario.exception.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorDTO {

    private String mensagem;
    private Integer statusHttp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    public ErrorDTO(String mensagem, Integer statusHttp) {
        this.mensagem = mensagem;
        this.statusHttp = statusHttp;
        this.dateTime = LocalDateTime.now();
    }
}
