package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.FuncaoEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.NacionalidadeEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = new Validator();
    }

    // Teste para validação de funcionário
    @Test
    void testValidateFuncionarioComCamposInvalidos() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(""); // Nome inválido
        funcionario.setIdade(null); // Idade inválida
        funcionario.setSenha("123"); // Senha válida
        funcionario.setConfirmacaoSenha("321"); // Confirmação de senha não bate
        funcionario.setEmail("email_invalido"); // Email inválido
        funcionario.setCpf("111.111.111-11"); // CPF inválido
        funcionario.setFuncao(FuncaoEnum.ADMINISTRATIVO);

        // Verifica se uma exceção de validação é lançada
        assertThrows(ValidacaoException.class, () -> validator.validateFuncionario(funcionario));
    }

    @Test
    void testValidateFuncionarioComCamposValidos() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("João Silva"); // Nome válido
        funcionario.setIdade(30); // Idade válida
        funcionario.setSenha("senha123"); // Senha válida
        funcionario.setConfirmacaoSenha("senha123"); // Confirmação de senha bate
        funcionario.setEmail("joao.silva@example.com"); // Email válido
        funcionario.setCpf("123.456.789-09"); // CPF válido
        funcionario.setFuncao(FuncaoEnum.ADMINISTRATIVO); // Função válida

        // Este não deve lançar uma exceção
        validator.validateFuncionario(funcionario);
    }

    // Teste para validação de ciclista
    @Test
    void testValidateCiclistaComDocumentosInvalidos() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Maria"); // Nome válido
        ciclista.setNascimento("2000-01-01"); // Data de nascimento válida
        ciclista.setNacionalidade(NacionalidadeEnum.BRASILEIRO); // Nacionalidade válida
        ciclista.setEmail("maria@gmail.com"); // Email válido
        ciclista.setSenha("senha123"); // Senha válida
        ciclista.setCpf(null); // CPF não informado, mas nacionalidade é brasileira
        ciclista.setPassaporte(null); // Passaporte não informado

        // Verifica se uma exceção de validação é lançada
        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCiclistaComCamposValidos() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Carlos Silva"); // Nome válido
        ciclista.setNascimento("1995-05-15"); // Data de nascimento válida
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO); // Nacionalidade válida
        ciclista.setEmail("carlos.silva@example.com"); // Email válido
        ciclista.setSenha("senhaSegura123"); // Senha válida

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("ABC123456");
        passaporte.setValidade("2030-01-01");
        passaporte.setPais("Brasil");

        ciclista.setCpf(null); // CPF não informado, mas passaporte está presente
        ciclista.setPassaporte(passaporte); // Passaporte válido

        // Este não deve lançar uma exceção
        validator.validateCiclista(ciclista);
    }


}
