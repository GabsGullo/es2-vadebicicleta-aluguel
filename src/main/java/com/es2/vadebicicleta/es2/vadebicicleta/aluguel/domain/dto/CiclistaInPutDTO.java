package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class CiclistaInPutDTO {
    @NotBlank
    private String nome;
    @NotBlank
    private String nascimento;
    @CPF
    private String cpf;
    private Ciclista.Passaporte passaporte;
    @NotBlank
    private String nacionalidade;
    @Email
    private String email;
    private String urlFotoDocumento;
}
