package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class IdGenerator {
    private Integer idCiclista = 0;
    private Integer idCartao = 0;
    private Integer idFuncionario = 0;
    private Integer matricula = 0;

    public Integer generateIdCiclista(){
       idCiclista = idCiclista + 1;
       return idCiclista;
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

}
