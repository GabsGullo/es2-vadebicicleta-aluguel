package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoEmail {

    private String assunto;
    private Long id;
    private String mensagem;
    private String email;


}
