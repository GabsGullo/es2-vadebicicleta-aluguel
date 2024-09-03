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
public class DevolucaoRegistroDTO {

    @NotNull
    private Integer idTranca;

    @NotNull
    private Integer idBicicleta;
}
