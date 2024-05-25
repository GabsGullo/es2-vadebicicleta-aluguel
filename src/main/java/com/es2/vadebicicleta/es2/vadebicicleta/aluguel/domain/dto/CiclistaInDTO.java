package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CiclistaInDTO {
    private String nome;
    private String nascimento;
    private String cpf;
    private Ciclista.Passaporte passaporte;
    private String nacionalidade;
    private String email;
    private String urlFotoDocumento;
}
