package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.Exception;

public class UnprocessableEntityException extends RuntimeException{

    private String mensagem;
    private String codigo;

    public UnprocessableEntityException(String mensagem, String codigo) {
        super(mensagem);
        this.mensagem = mensagem;
        this.codigo = codigo;
    }
}
