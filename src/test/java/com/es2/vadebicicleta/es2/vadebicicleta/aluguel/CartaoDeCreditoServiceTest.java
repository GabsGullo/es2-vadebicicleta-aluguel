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

    private CartaoDeCredito criarCartaoDeCredito(Integer id, String nomeTitular, String numero, String validade, String cvv) {
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
        CartaoDeCredito cartao = criarCartaoDeCredito(
                1,
                "",
                "12345678",
                "13/25",
                "12"
        );
        doThrow(ValidacaoException.class).when(validator).validateCartaoDeCredito(any(CartaoDeCredito.class));

        assertThrows(ValidacaoException.class, () -> service.register(cartao));
    }

    @Test
    void testRegisterCartaoComCamposValidos() {
        CartaoDeCredito cartao = criarCartaoDeCredito(
                1,
                "João Silva",
                "1234567812345678",
                "12/25",
                "123"
        );
        when(repository.save(any(CartaoDeCredito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.register(cartao);

        verify(repository, times(1)).save(cartao);
    }

    @Test
    void testUpdateCartaoComCamposInvalidos() {
        CartaoDeCredito cartaoNovo = criarCartaoDeCredito(
                1,
                "",
                "87654321",
                "00/22",
                "abc"
        );

        CartaoDeCredito cartaoCadastrado = criarCartaoDeCredito(
                1,
                "João Silva",
                "1234567812345678",
                "12/25",
                "123"
        );

        when(repository.findById(1)).thenReturn(Optional.of(cartaoCadastrado));
        doThrow(ValidacaoException.class).when(validator).validateCartaoDeCredito(any(CartaoDeCredito.class));

        assertThrows(ValidacaoException.class, () -> service.update(cartaoNovo, 1));
    }

    @Test
    void testUpdateCartaoComCamposValidos() {
        CartaoDeCredito cartaoNovo = criarCartaoDeCredito(
                1,
                "Maria Oliveira",
                "8765432187654321",
                "11/26",
                "321"
        );

        CartaoDeCredito cartaoCadastrado = criarCartaoDeCredito(
                1,
                "João Silva",
                "1234567812345678",
                "12/25",
                "123"
        );

        when(repository.findById(1)).thenReturn(Optional.of(cartaoCadastrado));

        service.update(cartaoNovo, 1);

        verify(repository, times(1)).save(cartaoCadastrado);
    }
}
