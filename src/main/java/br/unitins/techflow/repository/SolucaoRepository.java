package br.unitins.techflow.repository;

import br.unitins.techflow.model.Solucao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolucaoRepository extends JpaRepository<Solucao, Long> {
}