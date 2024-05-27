package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class CiclistaRepository {
    private static HashMap<Integer, Ciclista> registroCiclistas;
    private IdGenerator id;

    public Ciclista save(Ciclista ciclista){
        if(findById(ciclista.getId()).isPresent()){
            registroCiclistas.replace(ciclista.getId(), ciclista);
            return ciclista;
        }

        Integer idCiclista = id.addElement();

        try {
            ciclista.setId(idCiclista);
            registroCiclistas.put(idCiclista, ciclista);
        }catch (Exception e){
            return null;
        }

        return ciclista;
    }

    public Optional<Ciclista> findById(Integer idCiclista){
        return Optional.of(registroCiclistas.get(idCiclista));
    }
}
