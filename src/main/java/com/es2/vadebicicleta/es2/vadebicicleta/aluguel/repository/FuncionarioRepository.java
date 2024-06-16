package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class FuncionarioRepository {

    private static HashMap<Integer, Funcionario> registroFuncionarios = new HashMap<>();
    private final IdGenerator id;
    private final IdGenerator matricula;

    public FuncionarioRepository(IdGenerator id, IdGenerator matricula) {
        this.id = id;
        this.matricula = matricula;
    }

    public Funcionario save(Funcionario funcionario){
        if(findById(funcionario.getId()).isPresent()){
            registroFuncionarios.replace(funcionario.getId(), funcionario);
            return funcionario;
        }

        Integer idFuncionario = id.generateIdFuncionario();
        String matriculaNova = matricula.generateIdMatricula();

        funcionario.setId(idFuncionario);
        funcionario.setMatricula(matriculaNova);
        registroFuncionarios.put(idFuncionario, funcionario);

        return funcionario;
    }

    public Optional<Funcionario> findById(Integer id){
        return Optional.ofNullable(registroFuncionarios.get(id));
    }

    public List<Funcionario> getAllFuncionarios(){
        List<Funcionario> listaFuncionarios = new ArrayList<>();
        listaFuncionarios.addAll(registroFuncionarios.values());
        return listaFuncionarios;
    }

    public Object delete(Integer idFuncionario){
        return registroFuncionarios.remove(idFuncionario);
    }
}
