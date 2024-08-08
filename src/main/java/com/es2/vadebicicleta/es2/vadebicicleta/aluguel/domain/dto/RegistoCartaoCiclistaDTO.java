package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class RegistoCartaoCiclistaDTO {
    @Valid
    private CiclistaInPostDTO ciclista;

    @Valid
    private CartaoDeCreditoDTO cartaoDeCredito;
}
