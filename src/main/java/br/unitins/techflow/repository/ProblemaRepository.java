package br.unitins.techflow.repository;

import br.unitins.techflow.model.Problema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemaRepository extends JpaRepository<Problema, Long> {
}