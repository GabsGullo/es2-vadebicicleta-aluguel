package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Funcionario {
    private String matricula;
    private String senha;
    private String confirmacaoSenha;
    private String email;
    private Integer idade;
    private String funcao;
    private String cpf;
}
