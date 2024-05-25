package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class CiclistaRepository {
    private static HashMap<Integer, Ciclista> ciclistas;
    private static int contagem;

    public Ciclista save(Ciclista ciclista){
        contagem += 1;
        try {
            ciclista.setId(contagem);
            ciclistas.put(contagem, ciclista);
        }catch (Exception e){
            return null;
        }
        return ciclista;
    }
    public Optional<Ciclista> findById(Integer id){
        return Optional.of(ciclistas.get(id));
    }
}
