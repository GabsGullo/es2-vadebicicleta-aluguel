package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FuncionarioInDTO {
    private String senha;
    private String confirmacaoSenha;
    private String email;
    private String nome;
    private Integer idade;
    private String funcao;
    private String cpf;
}
