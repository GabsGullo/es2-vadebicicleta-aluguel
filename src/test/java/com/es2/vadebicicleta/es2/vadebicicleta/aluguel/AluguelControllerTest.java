package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.AluguelController;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.AluguelDevolucaoConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelDevolucaoDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelOutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelRegistroDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.DevolucaoRegistroDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.AluguelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AluguelControllerTest {

    @Mock
    private AluguelService aluguelService;

    @Mock
    private AluguelDevolucaoConverter aluguelDevolucaoConverter;

    @InjectMocks
    private AluguelController aluguelController;


    @Test
    void testRealizarAluguel() {
        // Arrange
        Integer ciclista = 1;
        Integer tranca = 1;
        Aluguel aluguel = new Aluguel();
        AluguelOutDTO aluguelOutDTO = new AluguelOutDTO();
        AluguelRegistroDTO aluguelRegistroDTO = new AluguelRegistroDTO(ciclista, tranca);

        when(aluguelService.realizarAluguel(ciclista, tranca)).thenReturn(aluguel);
        when(aluguelDevolucaoConverter.aluguelToOutDTO(aluguel)).thenReturn(aluguelOutDTO);

        // Act
        ResponseEntity<AluguelOutDTO> response = aluguelController.realizarAluguel(aluguelRegistroDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aluguelOutDTO, response.getBody());
    }

    @Test
    void testRealizarDevolucao() {
        // Arrange
        Integer idTranca = 1;
        Integer idBicicleta = 1;
        Aluguel aluguel = new Aluguel();
        AluguelDevolucaoDTO devolucaoDTO = new AluguelDevolucaoDTO();
        DevolucaoRegistroDTO devolucaoRegistroDTO = new DevolucaoRegistroDTO(idTranca, idBicicleta);

        when(aluguelService.realizarDevolucao(idTranca, idBicicleta)).thenReturn(aluguel);
        when(aluguelDevolucaoConverter.aluguelToDevolucao(aluguel)).thenReturn(devolucaoDTO);

        // Act
        ResponseEntity<AluguelDevolucaoDTO> response = aluguelController.realizarDevolucao(devolucaoRegistroDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(devolucaoDTO, response.getBody());
    }
}
