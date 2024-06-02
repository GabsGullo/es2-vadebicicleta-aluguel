package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.MeioDePagamento;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CiclistaInPostDTO {
    @NotNull
    private String nome;
    @NotNull
    private String nascimento;
    @CPF
    private String cpf;
    @NotNull
    private Ciclista.Passaporte passaporte;
    @NotNull
    private String nacionalidade;
    @Email
    private String email;
    private String urlFotoDocumento;
    @NotNull
    private String senha;
    @NotNull
    private MeioDePagamento meioDePagamento;
}
