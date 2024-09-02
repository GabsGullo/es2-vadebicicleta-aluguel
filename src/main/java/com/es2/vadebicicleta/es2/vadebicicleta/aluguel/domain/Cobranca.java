package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Cobranca {
    Long id;
    Long ciclista;
    BigDecimal valor;
    StatusPagamentoEnum status;
    LocalDateTime horaSolicitacao;
    LocalDateTime horaFinalizacao;
}
