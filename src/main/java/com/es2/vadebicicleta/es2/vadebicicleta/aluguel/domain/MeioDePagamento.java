package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeioDePagamento{
    @NotNull
    private String nomeDoTitular;
    @NotNull
    private String numero;
    @NotNull
    private String validade;
    @Size(min = 3,max = 3)
    private String cvv;
}
