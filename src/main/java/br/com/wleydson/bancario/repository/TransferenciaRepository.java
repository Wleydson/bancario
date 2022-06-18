package br.com.wleydson.bancario.repository;

import br.com.wleydson.bancario.model.Conta;
import br.com.wleydson.bancario.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    List<Transferencia> findByConta(Conta conta);
}
