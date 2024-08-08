package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class AluguelRepository {

    private static final HashMap<Integer, Aluguel> registroAlugueis = new HashMap<>();

    public Aluguel register(Aluguel aluguel){
        if(findById(aluguel.getBicicleta()).isPresent()){
            registroAlugueis.replace(aluguel.getBicicleta(), aluguel);
            return aluguel;
        }

        registroAlugueis.put(aluguel.getBicicleta(), aluguel);
        return aluguel;
    }

    public Optional<Aluguel> findById(Integer id){
        return Optional.ofNullable(registroAlugueis.get(id));
    }

}
