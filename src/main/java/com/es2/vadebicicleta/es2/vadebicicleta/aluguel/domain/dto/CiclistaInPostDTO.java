package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.NacionalidadeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CiclistaInPostDTO {
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
    @NotBlank
    private String senha;
}
