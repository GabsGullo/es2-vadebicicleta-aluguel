package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CartaoDeCreditoDTO;

import org.springframework.stereotype.Component;

@Component
public class CartaoDeCreditoConverter {

    public CartaoDeCredito inDtoToEntity(CartaoDeCreditoDTO dto){
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();

        cartaoDeCredito.setNomeTitular(dto.getNomeTitular());
        cartaoDeCredito.setNumero(dto.getNumero());
        cartaoDeCredito.setValidade(dto.getValidade());
        cartaoDeCredito.setCvv(dto.getCvv());

        return cartaoDeCredito;
    }

    public CartaoDeCreditoDTO entityToOutDto(CartaoDeCredito cartaoDeCredito){
        CartaoDeCreditoDTO cartaoDeCreditoDTO = new CartaoDeCreditoDTO();

        cartaoDeCreditoDTO.setNomeTitular(cartaoDeCredito.getNomeTitular());
        cartaoDeCreditoDTO.setNumero(cartaoDeCredito.getNumero());
        cartaoDeCreditoDTO.setValidade(cartaoDeCredito.getValidade());
        cartaoDeCreditoDTO.setCvv(cartaoDeCredito.getCvv());

        return cartaoDeCreditoDTO;
    }
}
