package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Long cobranca;
    @NotNull
    private Integer ciclista;
}
