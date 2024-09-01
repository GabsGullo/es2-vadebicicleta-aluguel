package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Devolucao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class DevolucaoRepository {

    private static final HashMap<Integer, Devolucao> registroDevolucoes = new HashMap<>();
    private final IdGenerator id;

    public DevolucaoRepository(IdGenerator id) {
        this.id = id;
    }

    public Devolucao register(Devolucao devolucao){
        Integer idDevolucao = id.generateIdDevolucao();

        registroDevolucoes.put(idDevolucao, devolucao);
        return devolucao;
    }
}
