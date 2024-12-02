package br.anhembi.heal.repository;

import org.springframework.data.repository.CrudRepository;
import br.anhembi.heal.model.Paciente;

public interface PacienteRepo extends CrudRepository<Paciente, String> { // Usando o RG (String) como chave primária
    Paciente findByRg(String rg); // Método para encontrar paciente pelo RG
}