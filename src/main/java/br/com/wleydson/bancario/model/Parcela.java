package br.com.wleydson.bancario.model;

import br.com.wleydson.bancario.enumerations.StatusParcelaEnum;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parcelas")
public class Parcela {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor")
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name = "transferencia_id")
    @Setter(AccessLevel.NONE)
    private Transferencia transferencia;

    @Column(name = "data_criacao")
    @Setter(AccessLevel.NONE)
    private LocalDateTime dataCriacao;

    @Column(name = "data_agendada")
    private LocalDate dataAgendada;

    @Column(name = "data_transacao")
    private LocalDateTime dataTransacao;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusParcelaEnum status;

    @PrePersist
    public void preSave() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Parcela(Transferencia transferencia) {
        this.transferencia = transferencia;
    }
}
