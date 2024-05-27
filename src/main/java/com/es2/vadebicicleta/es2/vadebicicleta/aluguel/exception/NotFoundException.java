package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception;


public class NotFoundException extends RuntimeException{

    public NotFoundException() {}

    public NotFoundException(String mensagem, String codigo) {
        super(mensagem);
    }


}
