package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Aluguel {

    private Integer bicicleta;
    private String horaInicio;
    private Integer trancaFim;
    private String horaFim;
    private BigDecimal cobranca;
    private Integer ciclista;
    private Integer trancaInicio;
}

