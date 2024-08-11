package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Funcionario {
    private Integer id;
    private String matricula;
    private String senha;
    private String confirmacaoSenha;
    private String email;
    private String nome;
    private Integer idade;
    private FuncaoEnum funcao;
    private String cpf;
}
