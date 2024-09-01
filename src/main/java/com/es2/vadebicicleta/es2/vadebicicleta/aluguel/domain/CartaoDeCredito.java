package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CartaoDeCredito {
    @NotNull
    private Integer id;
    @NotNull
    private String nomeTitular;
    @NotNull
    private String numero;
    @NotNull
    private Integer idCiclista;
    @NotNull
    private LocalDate validade;
    @Size(min = 3,max = 3)
    private String cvv;
}
