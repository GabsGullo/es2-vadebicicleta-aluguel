package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class IdGenerator {
    private Integer idCiclista = 0;
    private Integer idCartao = 0;
    private Integer idFuncionario = 0;
    private Integer matricula = 0;
    private Integer idAluguel = 0;
    private Integer idDevolucao = 0;

    public Integer generateIdCiclista(){
       idCiclista = idCiclista + 1;
       return idCiclista;
    }

    public Integer generateIdDevolucao(){
        idDevolucao = idDevolucao + 1;
        return idDevolucao;
    }

    public Integer generateIdCartao(){
        idCartao =  idCartao + 1;
        return idCartao;
    }

    public Integer generateIdFuncionario(){
        idFuncionario = idFuncionario + 1;
        return idFuncionario;
    }

    public String generateIdMatricula(){
        matricula = matricula + 1;
        return matricula.toString();
    }

    public Integer generateIdAluguel(){
        idAluguel = idAluguel + 1;
        return idAluguel;
    }

}
