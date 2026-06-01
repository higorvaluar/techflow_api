package br.unitins.techflow.repository;

import br.unitins.techflow.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

    Optional<Tecnico> findByEmailIgnoreCase(String email);
}