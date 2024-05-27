package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception;

public class UnprocessableEntityException extends RuntimeException{

    private String mensagem;
    private String codigo;

    public UnprocessableEntityException(String mensagem, String codigo) {
        super(mensagem);
        this.mensagem = mensagem;
        this.codigo = codigo;
    }
}
