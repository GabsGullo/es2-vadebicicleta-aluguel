package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Aluguel {

    private Integer bicicleta;
    private String horaInicio;
    private Integer trancaFim;
    private String horaFim;
    private Integer cobranca;
    private Integer ciclista;
    private Integer trancaInicio;
}

