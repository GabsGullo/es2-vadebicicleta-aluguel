package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelDevolucaoDTO;
import org.springframework.stereotype.Component;

@Component
public class AluguelDevolucaoConverter {

    public AluguelDevolucaoDTO aluguelToDevolucao(Aluguel aluguel){
        AluguelDevolucaoDTO devolucaoDTO = new AluguelDevolucaoDTO();

        devolucaoDTO.setBicicleta(aluguel.getBicicleta());
        devolucaoDTO.setHoraInicio(aluguel.getHoraInicio());
        devolucaoDTO.setTrancaFim(aluguel.getTrancaFim());
        devolucaoDTO.setHoraFim(aluguel.getHoraFim());
        devolucaoDTO.setCobranca(aluguel.getCobranca());
        devolucaoDTO.setCiclista(aluguel.getCiclista());

        return devolucaoDTO;
    }
}
