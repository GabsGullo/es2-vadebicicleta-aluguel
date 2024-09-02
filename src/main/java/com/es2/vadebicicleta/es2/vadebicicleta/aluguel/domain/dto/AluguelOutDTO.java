package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AluguelOutDTO {

    private Integer bicicleta;
    private String horaInicio;
    private Integer trancaFim;
    private String horaFim;
    private Long cobranca;
    private Integer ciclista;
    private Integer trancaInicio;
}
