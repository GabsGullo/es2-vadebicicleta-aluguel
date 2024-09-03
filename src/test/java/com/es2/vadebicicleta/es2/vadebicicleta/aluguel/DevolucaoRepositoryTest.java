package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Devolucao;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.DevolucaoRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.IdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DevolucaoRepositoryTest {

    @InjectMocks
    private DevolucaoRepository devolucaoRepository;

    @Spy
    private IdGenerator idGenerator;

    @Test
    void TestSaveNewDevolucao() {
        // Configurando um ID falso para a devolução
        Integer expectedId = 123;
        doReturn(expectedId).when(idGenerator).generateIdDevolucao();

        // Criando uma instância de Devolucao para o teste
        Devolucao devolucao = new Devolucao();
        devolucao.setIdAluguel(1);
        devolucao.setNumeroBicicleta(101);
        devolucao.setNumeroTranca(202);
        devolucao.setHoraDevolucao(LocalDateTime.now());
        devolucao.setHoraCobranca(LocalDateTime.now());
        devolucao.setValorExtra(BigDecimal.TEN);

        // Registrando a devolução
        Devolucao savedDevolucao = devolucaoRepository.register(devolucao);

        // Verificando se o ID gerado foi associado à devolução
        assertEquals(devolucao, savedDevolucao);

        // Verificando se o método generateIdDevolucao foi chamado uma vez
        verify(idGenerator, times(1)).generateIdDevolucao();
    }
}
