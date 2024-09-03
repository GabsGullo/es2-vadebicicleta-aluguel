package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AluguelRegistroDTO {

    @NotNull
    private Integer ciclista;

    @NotNull
    private Integer trancaInicio;
}
