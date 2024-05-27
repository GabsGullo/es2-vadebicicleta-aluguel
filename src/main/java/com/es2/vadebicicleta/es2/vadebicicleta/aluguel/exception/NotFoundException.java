package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{

    private final String codigo;

    public NotFoundException(String mensagem, String codigo) {
        super(mensagem);
        this.codigo = codigo;
    }

}

