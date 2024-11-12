package br.anhembi.eventosapp.repository;

import org.springframework.data.repository.CrudRepository;

import br.anhembi.eventosapp.model.Evento;

public interface EventoRepo extends CrudRepository<Evento, Long>{
    Evento findByCodigo(long codigo);
}
