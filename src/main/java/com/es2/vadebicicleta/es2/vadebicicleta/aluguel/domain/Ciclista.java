package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ciclista {
    private Integer id;
    private String status;
    private String nome;
    private String nascimento;
    private String cpf;
    private Passaporte passaporte;
    private String nacionalidade;
    private String email;
    private String urlFotoDocumento;
    private String senha;
    private MeioDePagamento meioDePagamento;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Passaporte{

        private String numero;
        private String validade;
        private String pais;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private class MeioDePagamento{

        private String nomeDoTitular;
        private String numero;
        private String validade;
        private String cvv;
    }
}
