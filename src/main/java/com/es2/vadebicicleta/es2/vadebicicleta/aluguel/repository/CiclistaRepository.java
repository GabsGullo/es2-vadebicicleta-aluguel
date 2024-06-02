package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class CiclistaRepository {
    private static HashMap<Integer, Ciclista> registroCiclistas = new HashMap<>();
    private final IdGenerator id;

    public CiclistaRepository(IdGenerator id) {
        this.id = id;
    }

    public Ciclista save(Ciclista ciclista){
        if(findById(ciclista.getId()).isPresent()){
            registroCiclistas.replace(ciclista.getId(), ciclista);
            return ciclista;
        }

        Integer idCiclista = id.addElement();

        ciclista.setId(idCiclista);
        ciclista.setStatus("AGUARDANDO_CONFIRMACAO");
        registroCiclistas.put(idCiclista, ciclista);

        return ciclista;
    }

    public Optional<Ciclista> findById(Integer idCiclista){
        return Optional.ofNullable(registroCiclistas.get(idCiclista));
    }
}
