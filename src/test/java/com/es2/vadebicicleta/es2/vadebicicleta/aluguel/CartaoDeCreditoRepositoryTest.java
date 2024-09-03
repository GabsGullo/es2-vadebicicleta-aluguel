package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CartaoDeCreditoRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.IdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartaoDeCreditoRepositoryTest {

    @InjectMocks
    private CartaoDeCreditoRepository cartaoDeCreditoRepository;
    @Spy
    private IdGenerator idGenerator;

    @Test
    void testSaveNewCartaoDeCredito() {
        // Configurando um ID falso para o novo cartão de crédito
        Integer expectedId = 1;
        doReturn(expectedId).when(idGenerator).generateIdCartao();

        // Criando um novo cartão de crédito
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setNomeTitular("João Silva");
        cartaoDeCredito.setNumero("1234567890123456");
        cartaoDeCredito.setIdCiclista(1);
        cartaoDeCredito.setValidade(LocalDate.of(2025, 12, 31));
        cartaoDeCredito.setCvv("123");

        // Salvando o cartão de crédito
        CartaoDeCredito savedCartaoDeCredito = cartaoDeCreditoRepository.save(cartaoDeCredito);

        // Verificando se o ID foi corretamente setado
        assertEquals(expectedId, savedCartaoDeCredito.getId());

        // Verificando se o método generateIdCartao foi chamado uma vez
        verify(idGenerator, times(1)).generateIdCartao();
    }

    @Test
    void testUpdateExistingCartaoDeCredito() {
        // Configurando um ID falso para o cartão de crédito existente
        Integer expectedId = 1;
        doReturn(expectedId).when(idGenerator).generateIdCartao();

        // Criando e salvando um cartão de crédito
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setId(expectedId);
        cartaoDeCredito.setNomeTitular("João Silva");
        cartaoDeCredito.setNumero("1234567890123456");
        cartaoDeCredito.setIdCiclista(1);
        cartaoDeCredito.setValidade(LocalDate.of(2025, 12, 31));
        cartaoDeCredito.setCvv("123");

        cartaoDeCreditoRepository.save(cartaoDeCredito);

        // Atualizando os dados do cartão de crédito
        cartaoDeCredito.setNomeTitular("João A. Silva");

        // Salvando novamente (deverá substituir o existente)
        CartaoDeCredito updatedCartaoDeCredito = cartaoDeCreditoRepository.save(cartaoDeCredito);

        // Verificando se o nome do titular foi atualizado
        assertEquals("João A. Silva", updatedCartaoDeCredito.getNomeTitular());

        // Verificando se o ID foi mantido
        assertEquals(expectedId, updatedCartaoDeCredito.getId());
    }

    @Test
    void testFindById() {
        // Configurando um ID falso para o cartão de crédito
        Integer expectedId = 1;

        // Criando e salvando um cartão de crédito
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setId(expectedId);
        cartaoDeCredito.setNomeTitular("João Silva");
        cartaoDeCredito.setNumero("1234567890123456");
        cartaoDeCredito.setIdCiclista(1);
        cartaoDeCredito.setValidade(LocalDate.of(2025, 12, 31));
        cartaoDeCredito.setCvv("123");

        cartaoDeCreditoRepository.save(cartaoDeCredito);

        // Buscando pelo ID do ciclista
        Optional<CartaoDeCredito> foundCartaoDeCredito = cartaoDeCreditoRepository.findById(1);

        // Verificando se o cartão foi encontrado
        assertTrue(foundCartaoDeCredito.isPresent());
        assertEquals(cartaoDeCredito, foundCartaoDeCredito.get());
    }

}
