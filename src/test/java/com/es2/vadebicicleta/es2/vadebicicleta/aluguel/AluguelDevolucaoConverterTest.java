package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.AluguelDevolucaoConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelDevolucaoDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelOutDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AluguelDevolucaoConverterTest {

    private final AluguelDevolucaoConverter converter = new AluguelDevolucaoConverter();

    @Test
    void testAluguelToDevolucao() {
        // Arrange
        Aluguel aluguel = new Aluguel();
        aluguel.setBicicleta(1);
        aluguel.setHoraInicio(LocalDateTime.of(2024, 9, 3, 10, 0));
        aluguel.setTrancaFim(2);
        aluguel.setHoraFim(LocalDateTime.of(2024, 9, 3, 12, 0));
        aluguel.setCobranca(10L);
        aluguel.setCiclista(1);

        AluguelDevolucaoDTO expectedDTO = new AluguelDevolucaoDTO();
        expectedDTO.setBicicleta(1);
        expectedDTO.setHoraInicio("2024-09-03T10:00");
        expectedDTO.setTrancaFim(2);
        expectedDTO.setHoraFim("2024-09-03T12:00");
        expectedDTO.setCobranca(10L);
        expectedDTO.setCiclista(1);

        // Act
        AluguelDevolucaoDTO resultDTO = converter.aluguelToDevolucao(aluguel);

        // Assert
        assertEquals(expectedDTO.getBicicleta(), resultDTO.getBicicleta());
        assertEquals(expectedDTO.getHoraInicio(), resultDTO.getHoraInicio());
        assertEquals(expectedDTO.getTrancaFim(), resultDTO.getTrancaFim());
        assertEquals(expectedDTO.getHoraFim(), resultDTO.getHoraFim());
        assertEquals(expectedDTO.getCobranca(), resultDTO.getCobranca());
        assertEquals(expectedDTO.getCiclista(), resultDTO.getCiclista());
    }

    @Test
    void testAluguelToOutDTO() {
        // Arrange
        Aluguel aluguel = new Aluguel();
        aluguel.setBicicleta(1);
        aluguel.setHoraInicio(LocalDateTime.of(2024, 9, 3, 10, 0));
        aluguel.setTrancaFim(2);
        aluguel.setHoraFim(LocalDateTime.of(2024, 9, 3, 12, 0));
        aluguel.setCobranca(10L);
        aluguel.setTrancaInicio(1);
        aluguel.setCiclista(1);

        AluguelOutDTO expectedDTO = new AluguelOutDTO();
        expectedDTO.setBicicleta(1);
        expectedDTO.setHoraInicio("2024-09-03T10:00");
        expectedDTO.setTrancaFim(2);
        expectedDTO.setHoraFim("2024-09-03T12:00");
        expectedDTO.setCobranca(10L);
        expectedDTO.setTrancaInicio(1);
        expectedDTO.setCiclista(1);

        // Act
        AluguelOutDTO resultDTO = converter.aluguelToOutDTO(aluguel);

        // Assert
        assertEquals(expectedDTO.getBicicleta(), resultDTO.getBicicleta());
        assertEquals(expectedDTO.getHoraInicio(), resultDTO.getHoraInicio());
        assertEquals(expectedDTO.getTrancaFim(), resultDTO.getTrancaFim());
        assertEquals(expectedDTO.getHoraFim(), resultDTO.getHoraFim());
        assertEquals(expectedDTO.getCobranca(), resultDTO.getCobranca());
        assertEquals(expectedDTO.getTrancaInicio(), resultDTO.getTrancaInicio());
        assertEquals(expectedDTO.getCiclista(), resultDTO.getCiclista());
    }
}
