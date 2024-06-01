package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioInDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.UnprocessableEntityException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;

    @Autowired
    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public List<Funcionario> getAllFuncionarios(){
        return repository.getAllFuncionarios();
    }

    public Funcionario getById(Integer idFuncionario){
        return repository.findById(idFuncionario).orElseThrow(
                () -> new NotFoundException("Funcionario não encontrado", HttpStatus.NOT_FOUND.toString()));
    }

    public Funcionario save(Funcionario funcionario){return repository.save(funcionario);}

    public Funcionario update(FuncionarioInDTO funcionarioNovo, Integer idFuncionario){
        Funcionario funcionarioCadastrado = getById(idFuncionario);

        funcionarioCadastrado.setSenha(funcionarioNovo.getSenha());
        funcionarioCadastrado.setConfirmacaoSenha(funcionarioNovo.getConfirmacaoSenha());
        funcionarioCadastrado.setEmail(funcionarioNovo.getEmail());
        funcionarioCadastrado.setNome(funcionarioNovo.getNome());
        funcionarioCadastrado.setIdade(funcionarioNovo.getIdade());
        funcionarioCadastrado.setFuncao(funcionarioNovo.getFuncao());
        funcionarioCadastrado.setCpf(funcionarioNovo.getCpf());

        return repository.save(funcionarioCadastrado);
    }

    public Object delete(Integer idFuncionario){
        if(idFuncionario < 0)
            throw new UnprocessableEntityException("Chave invalida", HttpStatus.UNPROCESSABLE_ENTITY.toString());
        Object retorno = repository.delete(idFuncionario);
        if(retorno == null){
            throw new NotFoundException("Funcionário não encontrado", HttpStatus.NOT_FOUND.toString());
        }

        return retorno;
    }
}