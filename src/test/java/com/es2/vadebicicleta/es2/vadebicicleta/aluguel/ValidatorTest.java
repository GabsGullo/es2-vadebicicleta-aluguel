package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.*;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = new Validator();
    }

    // Teste para validação de funcionário
    @Test
    void testValidateFuncionarioComCamposInvalidos() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("");
        funcionario.setIdade(null);
        funcionario.setSenha("123");
        funcionario.setConfirmacaoSenha("321");
        funcionario.setEmail("email_invalido");
        funcionario.setCpf("111.111.111-11");
        funcionario.setFuncao(FuncaoEnum.ADMINISTRATIVO);

        // Verifica se uma exceção de validação é lançada
        assertThrows(ValidacaoException.class, () -> validator.validateFuncionario(funcionario));
    }

    @Test
    void testValidateFuncionarioComCamposValidos() {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("João Silva");
        funcionario.setIdade(30);
        funcionario.setSenha("senha123");
        funcionario.setConfirmacaoSenha("senha123");
        funcionario.setEmail("joao.silva@example.com");
        funcionario.setCpf("123.456.789-09");
        funcionario.setFuncao(FuncaoEnum.ADMINISTRATIVO);

        // Este não deve lançar uma exceção
        validator.validateFuncionario(funcionario);
    }

    @Test
    void testValidateCiclistaComCamposValidos() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Carlos Silva");
        ciclista.setNascimento("1995-05-15");
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO);
        ciclista.setEmail("carlos.silva@example.com");
        ciclista.setSenha("123");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("ABC123456");
        passaporte.setValidade("2030-01-01");
        passaporte.setPais("Brasil");

        ciclista.setCpf(null);
        ciclista.setPassaporte(passaporte);

        // Este não deve lançar uma exceção
        validator.validateCiclista(ciclista);
    }

    @Test
    void testValidateCiclistaSemDocumento() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("João");
        ciclista.setNascimento("1980-01-01");
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO);
        ciclista.setEmail("joao@example.com");
        ciclista.setSenha("senhaForte123");
        ciclista.setCpf(null);
        ciclista.setPassaporte(null);


        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    // Teste para caso onde CPF e passaporte são informados
    @Test
    void testValidateCiclistaComCpfEPassaporte() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Ana Souza");
        ciclista.setNascimento("1990-03-20");
        ciclista.setNacionalidade(NacionalidadeEnum.BRASILEIRO);
        ciclista.setEmail("ana.souza@example.com");
        ciclista.setSenha("senhaSegura123");
        ciclista.setCpf("123.456.789-09");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("DEF987654");
        passaporte.setValidade("2028-07-01");
        passaporte.setPais("Brasil");

        ciclista.setPassaporte(passaporte);


        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    // Teste para caso onde CPF não é informado, mas a nacionalidade é brasileira
    @Test
    void testValidateCiclistaSemCpfComNacionalidadeBrasileira() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Pedro Lima");
        ciclista.setNascimento("1985-12-10");
        ciclista.setNacionalidade(NacionalidadeEnum.BRASILEIRO);
        ciclista.setEmail("pedro.lima@example.com");
        ciclista.setSenha("senhaMuitoSegura123");
        ciclista.setCpf(null);
        ciclista.setPassaporte(null);


        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    // Teste para caso onde CPF é informado, mas a nacionalidade não é brasileira
    @Test
    void testValidateCiclistaComCpfENacionalidadeEstrangeira() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Lucas");
        ciclista.setNascimento("2001-11-05");
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO);
        ciclista.setEmail("lucas@example.com");
        ciclista.setSenha("senhaSuperSegura123");
        ciclista.setCpf("987.654.321-00");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("GHI654321");
        passaporte.setValidade("2029-12-31");
        passaporte.setPais("Espanha");

        ciclista.setPassaporte(passaporte);

        // Verifica se uma exceção de validação é lançada
        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCiclistaComPassaporteNumeroNuloOuVazio() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Maria");
        ciclista.setNascimento("2000-01-01");
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO);
        ciclista.setEmail("maria@gmail.com");
        ciclista.setSenha("senha123");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("");
        passaporte.setValidade("2025-12-31");
        passaporte.setPais("Brasil");

        ciclista.setCpf(null);
        ciclista.setPassaporte(passaporte);

        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCiclistaComPassaporteValidadeNulaOuVazia() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Carlos");
        ciclista.setNascimento("1995-05-15");
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO);
        ciclista.setEmail("carlos.silva@example.com");
        ciclista.setSenha("senhaSegura123");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("ABC123456");
        passaporte.setValidade("");
        passaporte.setPais("Brasil");

        ciclista.setCpf(null);
        ciclista.setPassaporte(passaporte);

        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCiclistaComPassaportePaisNuloOuVazio() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Lucas");
        ciclista.setNascimento("2001-11-05");
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO);
        ciclista.setEmail("lucas@example.com");
        ciclista.setSenha("senhaSuperSegura123");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("ABC123456");
        passaporte.setValidade("2025-12-31");
        passaporte.setPais("");

        ciclista.setCpf(null);
        ciclista.setPassaporte(passaporte);

        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCiclistaComNascimentoNuloOuVazio() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Ana");
        ciclista.setNascimento("");
        ciclista.setNacionalidade(NacionalidadeEnum.BRASILEIRO);
        ciclista.setEmail("ana.silva@example.com");
        ciclista.setSenha("senhaForte123");
        ciclista.setCpf("123.456.789-09");
        ciclista.setPassaporte(null);

        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCiclistaComNacionalidadeNula() {
        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Pedro");
        ciclista.setNascimento("1990-06-15");
        ciclista.setNacionalidade(null);
        ciclista.setEmail("pedro.silva@example.com");
        ciclista.setSenha("senhaForte123");
        ciclista.setCpf("123.456.789-09");
        ciclista.setPassaporte(null);

        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCartaoDeCreditoComCamposInvalido() {
        CartaoDeCredito cartao = new CartaoDeCredito();
        cartao.setNomeTitular("João Silva");
        cartao.setNumero("12345");  // Número inválido
        cartao.setValidade("2025-12"); // Data invalida
        cartao.setCvv("12"); // cvv invalido

        assertThrows(ValidacaoException.class, () -> validator.validateCartaoDeCredito(cartao));
    }

//    @Test
//    void testValidateCartaoDeCreditoComValidadeInvalida() {
//        CartaoDeCredito cartao = new CartaoDeCredito();
//        cartao.setNomeTitular("João Silva");
//        cartao.setNumero("1234567812345678");
//        cartao.setValidade("2025-12");  // Validade inválida
//        cartao.setCvv("123");
//
//        assertThrows(ValidacaoException.class, () -> validator.validateCartaoDeCredito(cartao));
//    }
//
//    @Test
//    void testValidateCartaoDeCreditoComCvvInvalido() {
//        CartaoDeCredito cartao = new CartaoDeCredito();
//        cartao.setNomeTitular("João Silva");
//        cartao.setNumero("1234567812345678");
//        cartao.setValidade("12/25");
//        cartao.setCvv("12");  // CVV inválido
//
//        assertThrows(ValidacaoException.class, () -> validator.validateCartaoDeCredito(cartao));
//    }
//
//    @Test
//    void testValidateCartaoDeCreditoComCamposValidos() {
//        CartaoDeCredito cartao = new CartaoDeCredito();
//        cartao.setNomeTitular("João Silva");
//        cartao.setNumero("1234567812345678");
//        cartao.setValidade("12/25");
//        cartao.setCvv("123");
//
//        // Este não deve lançar uma exceção
//        validator.validateCartaoDeCredito(cartao);
//    }
}
