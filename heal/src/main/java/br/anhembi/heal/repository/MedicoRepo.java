package br.anhembi.heal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.anhembi.heal.model.Medico;

public interface MedicoRepo extends JpaRepository<Medico, Long>{
    Medico findByEmail(String email);
}
