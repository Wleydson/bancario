package br.com.wleydson.bancario.repository;

import br.com.wleydson.bancario.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByIdAndAtivoTrue(Long id);

    List<Conta> findByAtivoTrue();

    Optional<Conta> findByContaAndAtivoTrue(String conta);
}
