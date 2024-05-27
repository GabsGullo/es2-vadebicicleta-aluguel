package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception;

public class UnprocessableEntityException extends RuntimeException{

    public UnprocessableEntityException(String mensagem, String codigo) {
        super(mensagem);
    }
}
