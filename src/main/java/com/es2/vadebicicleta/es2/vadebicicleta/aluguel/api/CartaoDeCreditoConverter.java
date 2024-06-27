package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CartaoDeCreditoInDTO;

import org.springframework.stereotype.Component;

@Component
public class CartaoDeCreditoConverter {

    public CartaoDeCredito inDtoToEntity(CartaoDeCreditoInDTO dto){
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();

        cartaoDeCredito.setNomeTitular(dto.getNomeTitular());
        cartaoDeCredito.setNumero(dto.getNumero());
        cartaoDeCredito.setValidade(dto.getValidade());
        cartaoDeCredito.setCvv(dto.getCvv());

        return cartaoDeCredito;
    }
}
