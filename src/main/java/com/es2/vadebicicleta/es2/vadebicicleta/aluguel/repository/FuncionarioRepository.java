package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class FuncionarioRepository {

    private static HashMap<Integer, Funcionario> registroFuncionarios;
    private static int contagem;

    public Funcionario save(Funcionario funcionario){
        if(findById(funcionario.getId()).isPresent()){
            registroFuncionarios.replace(funcionario.getId(), funcionario);
            return funcionario;
        }

        contagem += 1;
        try {
            funcionario.setId(contagem);
            registroFuncionarios.put(contagem, funcionario);
        }catch (Exception e){
            return null;
        }

        return funcionario;
    }

    public Optional<Funcionario> findById(Integer id){
        return Optional.of(registroFuncionarios.get(id));
    }

    public ArrayList<Funcionario> getAllFuncionarios(){
        ArrayList<Funcionario> listaFuncionarios = new ArrayList<Funcionario>(registroFuncionarios.size());
        listaFuncionarios.addAll(registroFuncionarios.values());
        return listaFuncionarios;
    }

    public Object delete(Integer idFuncionario){
        if(findById(idFuncionario).isEmpty())
            return registroFuncionarios.remove(idFuncionario);
        else
            return null;
    }
}
