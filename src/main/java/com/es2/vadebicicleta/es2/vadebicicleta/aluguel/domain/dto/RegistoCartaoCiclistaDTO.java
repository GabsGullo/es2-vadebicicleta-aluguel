package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistoCartaoCiclistaDTO {
    @Valid
    private CiclistaInPostDTO ciclista;

    @Valid
    private CartaoDeCreditoDTO cartaoDeCredito;
}
