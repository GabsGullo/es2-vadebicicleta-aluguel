package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Ciclista {
    private Integer id;
    private StatusEnum status;
    @NotNull
    private String nome;
    @NotNull
    private LocalDate nascimento;
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Passaporte{
        @NotNull
        private String numero;
        @NotNull
        private String validade;
        @NotNull
        private String pais;
    }


}
