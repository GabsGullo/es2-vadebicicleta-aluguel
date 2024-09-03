package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.FuncaoEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioOutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioInDTO;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioConverter {

    public FuncionarioOutDTO entityToOutDTO(Funcionario funcionario){
        FuncionarioOutDTO dto = new FuncionarioOutDTO();

        dto.setMatricula(funcionario.getMatricula());
        dto.setNome(funcionario.getNome());
        dto.setCpf(funcionario.getCpf());
        dto.setEmail(funcionario.getEmail());
        dto.setSenha(funcionario.getSenha());
        dto.setConfirmacaoSenha(funcionario.getConfirmacaoSenha());
        dto.setIdade(funcionario.getIdade());
        dto.setFuncao(funcionario.getFuncao());

        return dto;
    }

    public Funcionario inDtoToEntity(FuncionarioInDTO dto){
        Funcionario funcionario = new Funcionario();

        funcionario.setSenha(dto.getPassword());
        funcionario.setConfirmacaoSenha(dto.getConfirmPassword());
        funcionario.setEmail(dto.getEmail());
        funcionario.setNome(dto.getNome());
        funcionario.setIdade(dto.getIdade());
        funcionario.setFuncao(FuncaoEnum.valueOf(dto.getFuncao()));
        funcionario.setCpf(dto.getCpf());

        return funcionario;
    }
}
