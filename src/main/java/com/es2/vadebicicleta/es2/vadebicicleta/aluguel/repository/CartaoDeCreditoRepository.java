package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class CartaoDeCreditoRepository {
    private static HashMap<Integer, CartaoDeCredito> registroCartao = new HashMap<>();
    private final IdGenerator id;

    public CartaoDeCreditoRepository(IdGenerator id) {
        this.id = id;
    }

    public CartaoDeCredito save(CartaoDeCredito cartaoDeCredito){
        if(findById(cartaoDeCredito.getId()).isPresent()){
            registroCartao.replace(cartaoDeCredito.getId(), cartaoDeCredito);
            return cartaoDeCredito;
        }

        Integer idCartao = id.generateIdCartao();

        cartaoDeCredito.setId(idCartao);
        registroCartao.put(idCartao, cartaoDeCredito);

        return cartaoDeCredito;
    }

    public Optional<CartaoDeCredito> findById(Integer idCiclista){
        return Optional.ofNullable(registroCartao.get(idCiclista));
    }
}
