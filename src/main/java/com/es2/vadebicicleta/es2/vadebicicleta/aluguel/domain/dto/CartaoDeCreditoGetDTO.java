package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartaoDeCreditoGetDTO {
    @NotNull
    private Integer id;
    @NotNull
    private String nomeTitular;
    @NotNull
    private String numero;
    @NotNull
    private String validade;
    @Size(min = 3,max = 3)
    private String cvv;
}
