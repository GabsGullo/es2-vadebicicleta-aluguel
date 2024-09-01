package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Aluguel {

    private Integer idAluguel;
    private Integer bicicleta;
    private LocalDateTime horaInicio;
    private Integer trancaFim;
    private LocalDateTime horaFim;
    private BigDecimal cobranca;
    private Integer ciclista;
    private Integer trancaInicio;
}

