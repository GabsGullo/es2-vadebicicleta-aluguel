package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AluguelDevolucaoDTO {

    @NotNull
    private Integer bicicleta;
    @NotNull
    private String horaInicio;
    @NotNull
    private Integer trancaFim;
    @NotNull
    private String horaFim;
    @NotNull
    private BigDecimal cobranca;
    @NotNull
    private Integer ciclista;
}
