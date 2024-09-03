package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.FuncionarioConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.FuncaoEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioInDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioOutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FuncionarioConverterTest {

    private FuncionarioConverter funcionarioConverter;

    @BeforeEach
    public void setUp() {
        funcionarioConverter = new FuncionarioConverter();
    }

    @Test
    public void testEntityToOutDTO() {
        Funcionario funcionario = new Funcionario();
        funcionario.setMatricula("12345");
        funcionario.setNome("João Silva");
        funcionario.setCpf("123.456.789-00");
        funcionario.setEmail("joao.silva@example.com");
        funcionario.setSenha("senha123");
        funcionario.setConfirmacaoSenha("senha123");
        funcionario.setIdade(30);
        funcionario.setFuncao(FuncaoEnum.REPARADOR);

        FuncionarioOutDTO dto = funcionarioConverter.entityToOutDTO(funcionario);

        assertEquals(funcionario.getMatricula(), dto.getMatricula());
        assertEquals(funcionario.getNome(), dto.getNome());
        assertEquals(funcionario.getCpf(), dto.getCpf());
        assertEquals(funcionario.getEmail(), dto.getEmail());
        assertEquals(funcionario.getSenha(), dto.getSenha());
        assertEquals(funcionario.getConfirmacaoSenha(), dto.getConfirmacaoSenha());
        assertEquals(funcionario.getIdade(), dto.getIdade());
        assertEquals(funcionario.getFuncao(), dto.getFuncao());
    }

    @Test
    public void testInDtoToEntity() {
        FuncionarioInDTO dto = new FuncionarioInDTO();
        dto.setEmail("joao.silva@example.com");
        dto.setNome("João Silva");
        dto.setIdade(30);
        dto.setFuncao(FuncaoEnum.REPARADOR);
        dto.setCpf("123.456.789-00");

        Funcionario funcionario = funcionarioConverter.inDtoToEntity(dto);

        assertEquals(dto.getEmail(), funcionario.getEmail());
        assertEquals(dto.getNome(), funcionario.getNome());
        assertEquals(dto.getIdade(), funcionario.getIdade());
        assertEquals(dto.getFuncao(), funcionario.getFuncao());
        assertEquals(dto.getCpf(), funcionario.getCpf());
    }
}
