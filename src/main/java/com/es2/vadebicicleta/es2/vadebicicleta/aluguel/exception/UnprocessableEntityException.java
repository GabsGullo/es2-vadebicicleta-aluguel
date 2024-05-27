package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception;

import lombok.Getter;

@Getter
public class UnprocessableEntityException extends RuntimeException{

    private String codigo;

    public UnprocessableEntityException(String mensagem, String codigo) {
        super(mensagem);
        this.codigo = codigo;
    }

}
