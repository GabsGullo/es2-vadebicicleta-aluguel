package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelDevolucaoDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelOutDTO;
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

    public AluguelOutDTO aluguelToOutDTO(Aluguel aluguel){

        AluguelOutDTO aluguelOutDTO = new AluguelOutDTO();

        aluguelOutDTO.setBicicleta(aluguel.getBicicleta());
        aluguelOutDTO.setCiclista(aluguel.getCiclista());
        aluguelOutDTO.setCobranca(aluguel.getCobranca());
        aluguelOutDTO.setTrancaFim(aluguel.getTrancaFim());
        aluguelOutDTO.setHoraInicio(aluguel.getHoraInicio());
        aluguelOutDTO.setHoraFim(aluguel.getHoraFim());
        aluguelOutDTO.setTrancaInicio(aluguel.getTrancaInicio());

        return aluguelOutDTO;
    }
}
