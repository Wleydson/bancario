package br.com.wleydson.bancario.model;

import br.com.wleydson.bancario.enumerations.StatusTransferenciaEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "transferencias")
public class Transferencia {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_transacao")
    private String numeroTransacao;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @Column(name = "conta_destino")
    private String contaDestino;

    @Setter(AccessLevel.NONE)
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusTransferenciaEnum status;

    @OneToMany(mappedBy = "transferencia")
    private List<Parcela> parcelas;

    public Transferencia() {
        this.parcelas = new ArrayList<>();
    }

    @PrePersist
    public void preSave() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }
}
