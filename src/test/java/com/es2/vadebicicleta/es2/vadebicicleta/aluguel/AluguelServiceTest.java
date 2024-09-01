package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao.ExternoClient;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.AluguelRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.DevolucaoRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.AluguelService;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CartaoDeCreditoService;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CiclistaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AluguelServiceTest {
    @Mock
    private AluguelRepository repository;

    @Mock
    private DevolucaoRepository devolucaoRepository;

    @Mock
    private ExternoClient externoClient;

    @Mock
    private CiclistaService ciclistaService;

    @Mock
    private CartaoDeCreditoService cartaoDeCreditoService;

    @InjectMocks
    private AluguelService aluguelService;


    // Método de utilitário para criar um objeto Aluguel
    private Aluguel criarAluguel(Integer trancaInicio, Integer ciclista, Integer bicicleta, BigDecimal cobranca, LocalDateTime horaInicio, LocalDateTime horaFim) {
        return Aluguel.builder()
                .trancaInicio(trancaInicio)
                .horaInicio(horaInicio)
                .horaFim(horaFim)
                .cobranca(cobranca)
                .ciclista(ciclista)
                .bicicleta(bicicleta)
                .build();
    }

    @Test
    void testRealizarAluguel() {
        Integer ciclistaId = 1;
        Integer tranca = 123;
        BigDecimal cobranca = BigDecimal.TEN;

        when(ciclistaService.getById(ciclistaId)).thenReturn(Ciclista.builder().build());
        when(repository.register(any(Aluguel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Aluguel aluguel = aluguelService.realizarAluguel(ciclistaId, tranca);

        assertNotNull(aluguel);
        assertEquals(tranca, aluguel.getTrancaInicio());
        assertEquals(ciclistaId, aluguel.getCiclista());
        assertEquals(cobranca, aluguel.getCobranca());
    }

    @Test
    void testRealizarDevolucao() {
        Integer tranca = 123;
        Integer bicicleta = 456;
        LocalDateTime horaInicio = LocalDateTime.now().minusHours(3);
        BigDecimal valorExtra = BigDecimal.valueOf(10);

        Aluguel aluguel = criarAluguel(tranca, 1, bicicleta, BigDecimal.TEN, horaInicio, null);

        when(repository.findByBicicletaIdHoraFimAluguel(bicicleta, null)).thenReturn(Optional.of(aluguel));
        when(ciclistaService.getById(any(Integer.class))).thenReturn(Ciclista.builder().build());
        when(repository.register(any(Aluguel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Aluguel resultado = aluguelService.realizarDevolucao(tranca, bicicleta);

        assertNotNull(resultado);
        assertEquals(bicicleta, resultado.getBicicleta());
        assertEquals(tranca, resultado.getTrancaFim());
        assertEquals(valorExtra.add(BigDecimal.TEN), resultado.getCobranca());
    }
}
