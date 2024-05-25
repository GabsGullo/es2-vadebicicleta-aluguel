package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ciclista {
    private Integer id;
    private String status;
    private String nome;
    private String nascimento;
    private String cpf;
    private String nacionalidade;
    private String email;
    private String urlFotoDocumento;
    private Passaporte passaporte;

    @Getter
    @Setter
    private static class Passaporte{
        public Passaporte(){}

        public Passaporte(String numero, String validade, String pais) {
            this.numero = numero;
            this.validade = validade;
            this.pais = pais;
        }

        private String numero;
        private String validade;
        private String pais;
    }

    public Ciclista() {
    }

    public Ciclista(String status, String nome, String nascimento, String cpf, String nacionalidade, String email, String urlFotoDocumento, Passaporte passaporte) {
        this.status = status;
        this.nome = nome;
        this.nascimento = nascimento;
        this.cpf = cpf;
        this.nacionalidade = nacionalidade;
        this.email = email;
        this.urlFotoDocumento = urlFotoDocumento;
        this.passaporte = passaporte;
    }
}
