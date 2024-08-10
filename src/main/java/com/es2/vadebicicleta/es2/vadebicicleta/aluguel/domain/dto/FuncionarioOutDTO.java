package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioOutDTO {
        private String matricula;
        private String senha;
        private String confirmacaoSenha;
        private String email;
        private String nome;
        private Integer idade;
        private String funcao;
        private String cpf;
}
