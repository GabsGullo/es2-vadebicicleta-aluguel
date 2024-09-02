package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Cobranca;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.StatusPagamentoEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CobrancaDTO;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CobrancaConverter {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;

    public Cobranca cobrancaDTOtoEntity(CobrancaDTO cobrancaDTO){
        Cobranca cobranca = new Cobranca();

        cobranca.setCiclista(cobrancaDTO.getCiclista());
        cobranca.setStatus(StatusPagamentoEnum.valueOf(cobrancaDTO.getStatus()));
        cobranca.setValor(cobrancaDTO.getValor());
        cobranca.setId(cobrancaDTO.getId());
        cobranca.setHoraFinalizacao(LocalDateTime.parse(cobrancaDTO.getHoraFinalizacao(), dateTimeFormatter));
        cobranca.setHoraSolicitacao(LocalDateTime.parse(cobrancaDTO.getHoraSolicitacao(), dateTimeFormatter));

        return cobranca;
    }
}
