package br.anhembi.eventosapp.repository;

import org.springframework.data.repository.CrudRepository;

import br.anhembi.eventosapp.model.Convidado;
import br.anhembi.eventosapp.model.Evento;

public interface ConvidadoRepo extends CrudRepository<Convidado, String> {
    Iterable<Convidado> findByEvento(Evento evento);
    Convidado findByRg(String rg);
}
