package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.Exception;


public class NotFoundException extends RuntimeException{

    private String mensagem;
    private String codigo;

    public NotFoundException() {}

    public NotFoundException(String mensagem, String codigo) {
        super(mensagem);
        this.mensagem = mensagem;
        this.codigo = codigo;
    }


}
