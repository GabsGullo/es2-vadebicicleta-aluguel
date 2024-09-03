package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.CartaoDeCreditoConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CartaoDeCreditoDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CartaoDeCreditoGetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CartaoDeCreditoConverterTest {

    private CartaoDeCreditoConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CartaoDeCreditoConverter();
    }

    @Test
    void testInDtoToEntity() {
        // Arrange
        CartaoDeCreditoDTO dto = new CartaoDeCreditoDTO(
                "John Doe",
                "1234567890123456",
                "2025-12-31",
                "123"
        );

        // Act
        CartaoDeCredito cartaoDeCredito = converter.inDtoToEntity(dto);

        // Assert
        assertNotNull(cartaoDeCredito);
        assertEquals("John Doe", cartaoDeCredito.getNomeTitular());
        assertEquals("1234567890123456", cartaoDeCredito.getNumero());
        assertEquals(LocalDate.of(2025, 12, 31), cartaoDeCredito.getValidade());
        assertEquals("123", cartaoDeCredito.getCvv());
    }


    @Test
    void testEntityToOutDto() {
        // Arrange
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
        cartaoDeCredito.setId(1);
        cartaoDeCredito.setNomeTitular("John Doe");
        cartaoDeCredito.setNumero("1234567890123456");
        cartaoDeCredito.setValidade(LocalDate.of(2025, 12, 31));
        cartaoDeCredito.setCvv("123");

        // Act
        CartaoDeCreditoGetDTO dto = converter.entityToOutDto(cartaoDeCredito);

        // Assert
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("John Doe", dto.getNomeTitular());
        assertEquals("1234567890123456", dto.getNumero());
        assertEquals("2025-12-31", dto.getValidade());
        assertEquals("123", dto.getCvv());
    }
}

