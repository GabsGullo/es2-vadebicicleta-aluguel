package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CiclistaOutDTO {
    private Integer id;
    private String status;
    private String nome;
    private String nascimento;
    private String cpf;
    private Ciclista.Passaporte passaporte;
    private String nacionalidade;
    private String email;
    private String urlFotoDocumento;
}
