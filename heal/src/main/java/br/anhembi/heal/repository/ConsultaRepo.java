package br.anhembi.heal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.anhembi.heal.model.Consulta;
import br.anhembi.heal.model.Paciente;

import java.util.List;

public interface ConsultaRepo extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPaciente(Paciente paciente);

    // Método para encontrar consulta por código
    Consulta findByCodigo(long codigo); // Adicionar esse método!
}