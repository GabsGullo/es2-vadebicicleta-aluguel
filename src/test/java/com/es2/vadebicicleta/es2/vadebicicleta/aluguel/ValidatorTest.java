package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.*;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ValidatorTest {
    private Validator validator;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
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
        String nascimento = "1995-05-15";
        String validadePassaporte = "2030-01-01";

        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Carlos Silva");
        ciclista.setNascimento(LocalDate.parse(nascimento, dateTimeFormatter));
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO);
        ciclista.setEmail("carlos.silva@example.com");
        ciclista.setSenha("123");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("ABC123456");
        passaporte.setValidade(LocalDate.parse(validadePassaporte, dateTimeFormatter));
        passaporte.setPais("Brasil");

        ciclista.setCpf(null);
        ciclista.setPassaporte(passaporte);

        // Este não deve lançar uma exceção
        validator.validateCiclista(ciclista);
    }

    @Test
    void testValidateCiclistaSemDocumento() {
        String nascimento = "1980-01-01";

        Ciclista ciclista = new Ciclista();
        ciclista.setNome("João");
        ciclista.setNascimento(LocalDate.parse(nascimento, dateTimeFormatter));
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
        String nascimento = "1990-03-20";
        String validadePassaporte = "2028-07-01";

        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Ana Souza");
        ciclista.setNascimento(LocalDate.parse(nascimento, dateTimeFormatter));
        ciclista.setNacionalidade(NacionalidadeEnum.BRASILEIRO);
        ciclista.setEmail("ana.souza@example.com");
        ciclista.setSenha("senhaSegura123");
        ciclista.setCpf("123.456.789-09");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("DEF987654");
        passaporte.setValidade(LocalDate.parse(validadePassaporte, dateTimeFormatter));
        passaporte.setPais("Brasil");

        ciclista.setPassaporte(passaporte);


        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    // Teste para caso onde CPF não é informado, mas a nacionalidade é brasileira
    @Test
    void testValidateCiclistaSemCpfComNacionalidadeBrasileira() {
        String nascimento = "1985-12-10";

        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Pedro Lima");
        ciclista.setNascimento(LocalDate.parse(nascimento, dateTimeFormatter));
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
        String nascimento = "2001-11-05";
        String validadePassaporte = "2029-12-31";

        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Lucas");
        ciclista.setNascimento(LocalDate.parse(nascimento, dateTimeFormatter));
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO);
        ciclista.setEmail("lucas@example.com");
        ciclista.setSenha("senhaSuperSegura123");
        ciclista.setCpf("987.654.321-00");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("GHI654321");
        passaporte.setValidade(LocalDate.parse(validadePassaporte, dateTimeFormatter));
        passaporte.setPais("Espanha");

        ciclista.setPassaporte(passaporte);

        // Verifica se uma exceção de validação é lançada
        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCiclistaComPassaporteNumeroNuloOuVazio() {
        String nascimento = "2000-01-01";
        String validadePassaporte = "2025-12-31";

        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Maria");
        ciclista.setNascimento(LocalDate.parse(nascimento, dateTimeFormatter));
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO);
        ciclista.setEmail("maria@gmail.com");
        ciclista.setSenha("senha123");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("");
        passaporte.setValidade(LocalDate.parse(validadePassaporte, dateTimeFormatter));
        passaporte.setPais("Brasil");

        ciclista.setCpf(null);
        ciclista.setPassaporte(passaporte);

        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCiclistaComPassaporteValidadeNulaOuVazia() {
        String nascimento = "1995-05-15";
        String validadePassaporte = "";

        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Carlos");
        ciclista.setNascimento(LocalDate.parse(nascimento, dateTimeFormatter));
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO);
        ciclista.setEmail("carlos.silva@example.com");
        ciclista.setSenha("senhaSegura123");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("ABC123456");
        passaporte.setValidade(null);
        passaporte.setPais("Brasil");

        ciclista.setCpf(null);
        ciclista.setPassaporte(passaporte);

        assertThrows(DateTimeParseException.class, () -> LocalDate.parse(validadePassaporte, dateTimeFormatter));
        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCiclistaComPassaportePaisNuloOuVazio() {
        String nascimento = "2001-11-05";
        String validadePassaporte = "2025-12-31";

        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Lucas");
        ciclista.setNascimento(LocalDate.parse(nascimento, dateTimeFormatter));
        ciclista.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO);
        ciclista.setEmail("lucas@example.com");
        ciclista.setSenha("senhaSuperSegura123");

        Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
        passaporte.setNumero("ABC123456");
        passaporte.setValidade(LocalDate.parse(validadePassaporte, dateTimeFormatter));
        passaporte.setPais("");

        ciclista.setCpf(null);
        ciclista.setPassaporte(passaporte);

        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCiclistaComNascimentoNuloOuVazio() {
        String nascimento = "";

        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Ana");
        ciclista.setNascimento(null);
        ciclista.setNacionalidade(NacionalidadeEnum.BRASILEIRO);
        ciclista.setEmail("ana.silva@example.com");
        ciclista.setSenha("senhaForte123");
        ciclista.setCpf("123.456.789-09");
        ciclista.setPassaporte(null);

        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
        assertThrows(DateTimeParseException.class, () -> LocalDate.parse(nascimento, DateTimeFormatter.ISO_DATE));
    }

    @Test
    void testValidateCiclistaComNacionalidadeNula() {
        String nascimento = "1990-06-15";

        Ciclista ciclista = new Ciclista();
        ciclista.setNome("Pedro");
        ciclista.setNascimento(LocalDate.parse(nascimento, dateTimeFormatter));
        ciclista.setNacionalidade(null);
        ciclista.setEmail("pedro.silva@example.com");
        ciclista.setSenha("senhaForte123");
        ciclista.setCpf("123.456.789-09");
        ciclista.setPassaporte(null);

        assertThrows(ValidacaoException.class, () -> validator.validateCiclista(ciclista));
    }

    @Test
    void testValidateCartaoDeCreditoComCamposInvalido() {
        String validade = "2025-12-01";

        CartaoDeCredito cartao = new CartaoDeCredito();
        cartao.setNomeTitular("João Silva");
        cartao.setNumero("12345");  // Número inválido
        cartao.setValidade(LocalDate.parse(validade, dateTimeFormatter)); // Data invalida
        cartao.setCvv("12"); // cvv invalido

        assertThrows(ValidacaoException.class, () -> validator.validateCartaoDeCredito(cartao));
    }

}
