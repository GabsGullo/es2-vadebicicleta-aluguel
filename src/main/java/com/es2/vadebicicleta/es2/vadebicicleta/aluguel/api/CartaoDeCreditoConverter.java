package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CartaoDeCreditoDTO;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CartaoDeCreditoGetDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CartaoDeCreditoConverter {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public CartaoDeCredito inDtoToEntity(CartaoDeCreditoDTO dto){
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();

        cartaoDeCredito.setNomeTitular(dto.getNomeTitular());
        cartaoDeCredito.setNumero(dto.getNumero());
        cartaoDeCredito.setValidade(LocalDate.parse(dto.getValidade(), dateTimeFormatter));
        cartaoDeCredito.setCvv(dto.getCvv());

        return cartaoDeCredito;
    }

    public CartaoDeCreditoGetDTO entityToOutDto(CartaoDeCredito cartaoDeCredito){
        CartaoDeCreditoGetDTO cartaoDeCreditoDTO = new CartaoDeCreditoGetDTO();

        cartaoDeCreditoDTO.setId(cartaoDeCredito.getId());
        cartaoDeCreditoDTO.setNomeTitular(cartaoDeCredito.getNomeTitular());
        cartaoDeCreditoDTO.setNumero(cartaoDeCredito.getNumero());
        cartaoDeCreditoDTO.setValidade(cartaoDeCredito.getValidade().format(dateTimeFormatter));
        cartaoDeCreditoDTO.setCvv(cartaoDeCredito.getCvv());

        return cartaoDeCreditoDTO;
    }
}
