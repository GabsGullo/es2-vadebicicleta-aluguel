package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class CiclistaInPostDTO {
    @NotNull
    private String nome;
    @NotNull
    private String nascimento;
    @CPF
    private String cpf;
    private Ciclista.Passaporte passaporte;
    @NotNull
    private String nacionalidade;
    @Email
    private String email;
    private String urlFotoDocumento;
    @NotBlank
    private String senha;
}
