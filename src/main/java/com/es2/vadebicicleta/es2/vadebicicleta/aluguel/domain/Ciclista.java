package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ciclista {
    private Integer id;
    private StatusEnum status;
    @NotNull
    private String nome;
    @NotNull
    private String nascimento;
    @CPF
    private String cpf;
    private Passaporte passaporte;
    @NotNull
    private NacionalidadeEnum nacionalidade;
    @Email
    private String email;
    private String urlFotoDocumento;
    @NotBlank
    private String senha;
    private Boolean aluguelAtivo;

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
