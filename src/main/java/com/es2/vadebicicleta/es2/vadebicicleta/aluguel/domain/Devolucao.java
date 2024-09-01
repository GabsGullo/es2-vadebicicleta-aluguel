package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Devolucao {

    private Integer id;
    private Integer idAluguel;
    private Integer numeroBicicleta;
    private Integer numeroTranca;
    private LocalDateTime horaDevolucao;
    private LocalDateTime horaCobranca;
    private BigDecimal valorExtra;
    private CartaoDeCredito cartaoDeCredito;

}
