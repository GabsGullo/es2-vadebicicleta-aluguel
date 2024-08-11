package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.validator.Validator;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.FuncaoEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.UnprocessableEntityException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.FuncionarioRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.FuncionarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FuncionarioServiceTest {
    @Mock
    private Validator validator;
    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private FuncionarioService funcionarioService;

    private Funcionario createFuncionario(Integer id, String matricula, String senha, String confirmacaoSenha, String email,
                                          String nome, Integer idade, FuncaoEnum funcao, String cpf){

        return Funcionario.builder()
                .id(id)
                .matricula(matricula)
                .senha(senha)
                .confirmacaoSenha(confirmacaoSenha)
                .email(email)
                .nome(nome)
                .idade(idade)
                .funcao(funcao)
                .cpf(cpf)
                .build();
    }

    @Test
    void testSaveFuncionario() {

        Funcionario funcionario = createFuncionario(1, "1", "123", "123", "arrascaeta@flamengo.com", "Arrascaeta",
                28, FuncaoEnum.REPARADOR, "123.456.789-09");

        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Funcionario result = funcionarioService.save(funcionario);

        assertEquals(funcionario.getId(), result.getId());
        assertEquals(funcionario.getMatricula(), result.getMatricula());
        assertEquals(funcionario.getConfirmacaoSenha(), result.getConfirmacaoSenha());
        assertEquals(funcionario.getSenha(), result.getSenha());
        assertEquals(funcionario.getEmail(), result.getEmail());
        assertEquals(funcionario.getNome(), result.getNome());
        assertEquals(funcionario.getIdade(), result.getIdade());
        assertEquals(funcionario.getFuncao(), result.getFuncao());
        assertEquals(funcionario.getCpf(), result.getCpf());

        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
    }

    @Test
    void testSaveFuncionarioTodosCamposNulos() {
        // Cria um Funcionario com todos os campos nulos ou inválidos
        Funcionario funcionario = createFuncionario(
                1,
                null,                // Matricula nula
                null,                // Senha nula
                null,                // ConfirmacaoSenha nula
                null,                // Email nulo
                null,                // Nome nulo
                null,                // Idade nula
                null,                // Funcao nula
                null                 // CPF nulo
        );

        doThrow(ValidacaoException.class).when(validator).validateFuncionario(any(Funcionario.class));
        // Espera uma exceção de validação
        assertThrows(ValidacaoException.class, () -> funcionarioService.save(funcionario));
    }

    @Test
    void testSaveFuncionarioComConfirmacaoSenhaDiferente() {
        Funcionario funcionario = createFuncionario(1, "1", "123", "456", "arrascaeta@flamengo.com", "Arrascaeta",
                28, FuncaoEnum.REPARADOR, "123.456.789-09");

        doThrow(ValidacaoException.class).when(validator).validateFuncionario(any(Funcionario.class));

        // Espera uma exceção de validação
        assertThrows(ValidacaoException.class, () -> funcionarioService.save(funcionario));
    }

    @Test
    void testNotfindFuncionarioById(){

        assertThrows(NotFoundException.class, () -> {
            funcionarioService.getById(1);
        });
    }

    @Test
    void testFindFuncionarioById() {
        Funcionario funcionario = createFuncionario(1, "1", "123", "123", "arrascaeta@flamengo.com", "Arrascaeta",
                28, FuncaoEnum.REPARADOR, "123.456.789-09");

        when(funcionarioRepository.findById(funcionario.getId())).thenReturn(Optional.of(funcionario));

        Funcionario result = funcionarioService.getById(1);

        assertEquals(funcionario.getId(), result.getId());
        assertEquals(funcionario.getMatricula(), result.getMatricula());
        assertEquals(funcionario.getConfirmacaoSenha(), result.getConfirmacaoSenha());
        assertEquals(funcionario.getSenha(), result.getSenha());
        assertEquals(funcionario.getEmail(), result.getEmail());
        assertEquals(funcionario.getNome(), result.getNome());
        assertEquals(funcionario.getIdade(), result.getIdade());
        assertEquals(funcionario.getFuncao(), result.getFuncao());
        assertEquals(funcionario.getCpf(), result.getCpf());
    }

    @Test
    void testUpdateFuncionario() {
        Funcionario funcionarioNovo = createFuncionario(
                1,"1","novaSenha",
                "novaSenha",
                "novoEmail@exemplo.com",
                "Novo Nome",
                31,
                FuncaoEnum.ADMINISTRATIVO,
                "110.089.390-30"
        );

        Funcionario funcionarioExistente = createFuncionario(
                1,
                "123",
                "senha",
                "senha",
                "email@exemplo.com",
                "Nome",
                30,
                FuncaoEnum.REPARADOR,
                "123.456.789-09"
        );

        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionarioExistente));
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Funcionario result = funcionarioService.update(funcionarioNovo, 1);

        assertEquals(funcionarioNovo.getSenha(), result.getSenha());
        assertEquals(funcionarioNovo.getConfirmacaoSenha(), result.getConfirmacaoSenha());
        assertEquals(funcionarioNovo.getEmail(), result.getEmail());
        assertEquals(funcionarioNovo.getNome(), result.getNome());
        assertEquals(funcionarioNovo.getIdade(), result.getIdade());
        assertEquals(funcionarioNovo.getFuncao(), result.getFuncao());
        assertEquals(funcionarioNovo.getCpf(), result.getCpf());

        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
    }

    @Test
    void testUpdateFuncionarioNotFound() {
        Funcionario funcionarioNovo = createFuncionario(
                1,"1","novaSenha",
                "novaConfirmacaoSenha",
                "novoEmail@exemplo.com",
                "Novo Nome",
                31,
                FuncaoEnum.ADMINISTRATIVO,
                "123.456.789-10"
        );

        when(funcionarioRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> funcionarioService.update(funcionarioNovo, 1));
    }

    @Test
    void testUpdateFuncionarioInvalidData() {
        Funcionario funcionarioNovo = createFuncionario(
                1,
                null,
                null,
                null,
                null,
                null, null,null,
                null
        );

        Funcionario funcionarioExistente = createFuncionario(
                1,
                "123",
                "senha",
                "senha",
                "email@exemplo.com",
                "Nome",
                30,
                FuncaoEnum.REPARADOR,
                "123.456.789-09"
        );

        when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionarioExistente));
        doThrow(ValidacaoException.class).when(validator).validateFuncionario(any(Funcionario.class));

        assertThrows(ValidacaoException.class, () -> funcionarioService.update(funcionarioNovo, 1));
    }

    @Test
    void testDeleteFuncionario() {
        when(funcionarioRepository.delete(1)).thenReturn(new Object());

        Object result = funcionarioService.delete(1);

        assertNotNull(result);
        verify(funcionarioRepository, times(1)).delete(1);
    }

    @Test
    void testDeleteFuncionarioIdInvalido() {
        assertThrows(UnprocessableEntityException.class, () -> funcionarioService.delete(-1));
    }

    @Test
    void testDeleteFuncionarioNotFound() {
        when(funcionarioRepository.delete(1)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> funcionarioService.delete(1));
    }
}
