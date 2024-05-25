package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class CiclistaRepository {
    private static HashMap<Integer, Ciclista> ciclistas;
    private static Integer contagem;

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
    public Ciclista findById(Integer id){
        return ciclistas.get(id);
    }
}
