package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class CobrancaDTO {
    Long id;
    String status;
    String horaSolicitacao;
    String horaFinalizacao;
    BigDecimal valor;
    Long ciclista;
}
