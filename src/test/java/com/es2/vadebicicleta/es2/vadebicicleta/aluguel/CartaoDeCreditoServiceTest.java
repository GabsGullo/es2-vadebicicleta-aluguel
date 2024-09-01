package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CartaoDeCreditoRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CartaoDeCreditoService;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.validator.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartaoDeCreditoServiceTest {

    @Mock
    private CartaoDeCreditoRepository repository;
    @Mock
    private Validator validator;

    @InjectMocks
    private CartaoDeCreditoService service;

    private CartaoDeCredito criarCartaoDeCredito(Integer id, String nomeTitular, String numero, LocalDate validade, String cvv) {
        CartaoDeCredito cartao = new CartaoDeCredito();
        cartao.setId(id);
        cartao.setNomeTitular(nomeTitular);
        cartao.setNumero(numero);
        cartao.setValidade(validade);
        cartao.setCvv(cvv);
        return cartao;
    }

    @Test
    void testRegisterCartaoComCamposInvalidos() {
        String validade = "2025-13-01";
        CartaoDeCredito cartao = criarCartaoDeCredito(
                1,
                "",
                "12345678",
                null,
                "12"
        );
        doThrow(ValidacaoException.class).when(validator).validateCartaoDeCredito(any(CartaoDeCredito.class));

        assertThrows(DateTimeParseException.class, () -> LocalDate.parse(validade, DateTimeFormatter.ISO_DATE));
        assertThrows(ValidacaoException.class, () -> service.register(cartao));
    }

    @Test
    void testRegisterCartaoComCamposValidos() {
        String validade = "2025-12-01";
        CartaoDeCredito cartao = criarCartaoDeCredito(
                1,
                "João Silva",
                "1234567812345678",
                LocalDate.parse(validade, DateTimeFormatter.ISO_DATE),
                "123"
        );
        when(repository.save(any(CartaoDeCredito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.register(cartao);

        verify(repository, times(1)).save(cartao);
    }

    @Test
    void testUpdateCartaoComCamposInvalidos() {
        // Cartão novo com campos inválidos
        String validade1 = "2025-12-01";
        CartaoDeCredito cartaoNovo = criarCartaoDeCredito(
                1,
                "", // Nome inválido
                "87654321", // Número de cartão inválido
                LocalDate.parse(validade1, DateTimeFormatter.ISO_DATE), // Data válida
                "abc" // Código de segurança inválido
        );

        // Cartão cadastrado existente
        String validade2 = "2025-12-01"; // Mudança do formato para ISO_DATE
        CartaoDeCredito cartaoCadastrado = criarCartaoDeCredito(
                1,
                "João Silva",
                "1234567812345678",
                LocalDate.parse(validade2, DateTimeFormatter.ISO_DATE), // Data válida
                "123" // Código de segurança válido
        );

        // Simula a recuperação do cartão cadastrado pelo ID
        when(repository.findById(1)).thenReturn(Optional.of(cartaoCadastrado));

        // Simula a exceção de validação
        doThrow(ValidacaoException.class).when(validator).validateCartaoDeCredito(any(CartaoDeCredito.class));

        // Teste para garantir que a validação de campos inválidos lança as exceções apropriadas
        assertThrows(ValidacaoException.class, () -> service.update(cartaoNovo, 1));
    }

    @Test
    void testUpdateCartaoComCamposValidos() {
        String validade = "2026-11-01";
        CartaoDeCredito cartaoNovo = criarCartaoDeCredito(
                1,
                "Maria Oliveira",
                "8765432187654321",
                LocalDate.parse(validade, DateTimeFormatter.ISO_DATE),
                "321"
        );

        validade = "2025-12-01";
        CartaoDeCredito cartaoCadastrado = criarCartaoDeCredito(
                1,
                "João Silva",
                "1234567812345678",
                LocalDate.parse(validade, DateTimeFormatter.ISO_DATE),
                "123"
        );

        when(repository.findById(1)).thenReturn(Optional.of(cartaoCadastrado));

        service.update(cartaoNovo, 1);

        verify(repository, times(1)).save(cartaoCadastrado);
    }
}
