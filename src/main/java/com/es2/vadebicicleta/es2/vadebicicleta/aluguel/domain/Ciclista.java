package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ciclista {
    private Integer id;
    private StatusEnum status;
    private String nome;
    private String nascimento;
    private String cpf;
    private Passaporte passaporte;
    private NacionalidadeEnum nacionalidade;
    private String email;
    private String urlFotoDocumento;
    private String senha;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Passaporte{
        @NotNull
        private String numero;
        @NotNull
        private String validade;
        @NotNull
        private String pais;
    }
}
