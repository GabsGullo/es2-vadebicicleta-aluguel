package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.Validator.Validator;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.UnprocessableEntityException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {
    public static final String OBJECT_NAME = "funcionario";
    private final FuncionarioRepository repository;
    private final Validator validator;

    @Autowired
    public FuncionarioService(FuncionarioRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public List<Funcionario> getAllFuncionarios(){
        return repository.getAllFuncionarios();
    }

    public Funcionario getById(Integer idFuncionario){
        return repository.findById(idFuncionario).orElseThrow(
                () -> new NotFoundException("Funcionario não encontrado", HttpStatus.NOT_FOUND.toString()));
    }

    public Funcionario save(Funcionario funcionario){
        validator.validateFuncionario(funcionario);

        return repository.save(funcionario);
    }

    public Funcionario update(Funcionario funcionarioNovo, Integer idFuncionario){
        Funcionario funcionarioCadastrado = getById(idFuncionario);

        alterarDados(funcionarioNovo, funcionarioCadastrado);

        validator.validateFuncionario(funcionarioCadastrado);

        return repository.save(funcionarioCadastrado);
    }

    private static void alterarDados(Funcionario funcionarioNovo, Funcionario funcionarioCadastrado) {
        funcionarioCadastrado.setSenha(funcionarioNovo.getSenha());
        funcionarioCadastrado.setConfirmacaoSenha(funcionarioNovo.getConfirmacaoSenha());
        funcionarioCadastrado.setEmail(funcionarioNovo.getEmail());
        funcionarioCadastrado.setNome(funcionarioNovo.getNome());
        funcionarioCadastrado.setIdade(funcionarioNovo.getIdade());
        funcionarioCadastrado.setFuncao(funcionarioNovo.getFuncao());
        funcionarioCadastrado.setCpf(funcionarioNovo.getCpf());
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