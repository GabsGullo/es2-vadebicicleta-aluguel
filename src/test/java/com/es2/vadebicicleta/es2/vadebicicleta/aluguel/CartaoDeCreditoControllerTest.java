package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.CartaoDeCreditoController;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.CartaoDeCreditoConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CartaoDeCreditoDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CartaoDeCreditoGetDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CartaoDeCreditoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartaoDeCreditoControllerTest {

    @Mock
    private CartaoDeCreditoService cartaoDeCreditoService;

    @Mock
    private CartaoDeCreditoConverter cartaoDeCreditoConverter;

    @InjectMocks
    private CartaoDeCreditoController cartaoDeCreditoController;

    @Test
    void testGetCartao() {
        // Arrange
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        CartaoDeCreditoGetDTO cartaoDeCreditoGetDTO = new CartaoDeCreditoGetDTO(); // Ajuste para o DTO correto
        when(cartaoDeCreditoService.getCartaoByCiclistaId(1)).thenReturn(cartaoDeCredito);
        when(cartaoDeCreditoConverter.entityToOutDto(cartaoDeCredito)).thenReturn(cartaoDeCreditoGetDTO);

        // Act
        ResponseEntity<CartaoDeCreditoGetDTO> response = cartaoDeCreditoController.getCartao(1);

        // Assert
        assertEquals(ResponseEntity.ok(cartaoDeCreditoGetDTO), response);
    }

    @Test
    void testPutCartaoDeCredito() {
        // Arrange
        CartaoDeCreditoDTO cartaoDTO = new CartaoDeCreditoDTO(
                "John Doe",
                "1234567890123456",
                "2025-12-31",
                "123"
        );

        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        when(cartaoDeCreditoConverter.inDtoToEntity(cartaoDTO)).thenReturn(cartaoDeCredito);

        // Simula que o cartão é atualizado com sucesso
        doNothing().when(cartaoDeCreditoService).update(cartaoDeCredito, 1);

        // Act
        ResponseEntity<CartaoDeCreditoDTO> response = cartaoDeCreditoController.putCartaoDeCredito(cartaoDTO, 1);

        // Assert
        assertEquals(ResponseEntity.ok().build(), response);
    }
}
