package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class CiclistaRepository {
    private final HashMap<Integer, Ciclista> registroCiclistas = new HashMap<>();
    private final IdGenerator id;

    public CiclistaRepository(IdGenerator id) {
        this.id = id;
    }

    public Ciclista save(Ciclista ciclista){
        if(findById(ciclista.getId()).isPresent()){
            registroCiclistas.replace(ciclista.getId(), ciclista);
            return ciclista;
        }

        Integer idCiclista = id.generateIdCiclista();

        ciclista.setId(idCiclista);
        registroCiclistas.put(idCiclista, ciclista);

        return ciclista;
    }

    public Optional<Ciclista> findById(Integer idCiclista){
        return Optional.ofNullable(registroCiclistas.get(idCiclista));
    }

    public void clear(){
        registroCiclistas.clear();
    }
}
