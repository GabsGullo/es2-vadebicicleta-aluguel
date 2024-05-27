package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {

    private String mensagem;
    private String codigo;
}
