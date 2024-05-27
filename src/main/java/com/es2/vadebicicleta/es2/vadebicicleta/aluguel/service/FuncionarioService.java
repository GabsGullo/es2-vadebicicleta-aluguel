package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioInDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;

    @Autowired
    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public ArrayList<Funcionario> getAllFuncionarios(){
        return repository.getAllFuncionarios();
    }

    public Optional<Funcionario> getById(Integer idFuncionario){return repository.findById(idFuncionario);}

    public Funcionario save(Funcionario funcionario){return repository.save(funcionario);}

    public Funcionario update(FuncionarioInDTO funcionarioNovo, Integer idFuncionario){
        Optional<Funcionario> funcionarioCadastrado = getById(idFuncionario);
        if(funcionarioCadastrado.isPresent()){
            Funcionario funcionarioAtualizado = funcionarioCadastrado.get();

            funcionarioAtualizado.setSenha(funcionarioNovo.getSenha());
            funcionarioAtualizado.setConfirmacaoSenha(funcionarioNovo.getConfirmacaoSenha());
            funcionarioAtualizado.setEmail(funcionarioNovo.getEmail());
            funcionarioAtualizado.setNome(funcionarioNovo.getNome());
            funcionarioAtualizado.setIdade(funcionarioNovo.getIdade());
            funcionarioAtualizado.setFuncao(funcionarioNovo.getFuncao());
            funcionarioAtualizado.setCpf(funcionarioNovo.getCpf());

            return repository.save(funcionarioAtualizado);
        }

        return null;
    }

    public Object delete(Integer idFuncionario){
        return repository.delete(idFuncionario);
    }
}
