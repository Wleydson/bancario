package br.com.wleydson.bancario.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private Long id;

    @Column(name = "agencia")
    @Setter(value = AccessLevel.NONE)
    private String agencia;

    @Column(name = "conta")
    private String conta;

    @Column(name = "saldo")
    @Setter(value = AccessLevel.NONE)
    private BigDecimal saldo;

    @Column(name = "data_criacao")
    @Setter(value = AccessLevel.NONE)
    private LocalDateTime dataCriacao;

    @Column(name = "ativo")
    @Setter(value = AccessLevel.NONE)
    private Boolean ativo;

    public void ativar() {
        this.ativo = Boolean.TRUE;
    }

    public void desativar() {
        this.ativo = Boolean.FALSE;
    }

    @PrePersist
    public void preSave() {
        this.ativo = true;
        this.dataCriacao = LocalDateTime.now();
        this.agencia = "0001";
        this.saldo = new BigDecimal(0);
    }

    public void subSaldo(BigDecimal valor){
        this.saldo = this.saldo.subtract(valor);
    }

    public void addSaldo(BigDecimal valor){
        this.saldo = this.saldo.add(valor);
    }
}
