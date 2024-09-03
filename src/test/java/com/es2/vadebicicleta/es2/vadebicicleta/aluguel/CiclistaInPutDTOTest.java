package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CiclistaInPutDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidDTO() {
        CiclistaInPutDTO dto = new CiclistaInPutDTO();
        dto.setNome("João Silva");
        dto.setNascimento("2000-01-01");
        dto.setCpf("123.456.789-09");
        dto.setNacionalidade("Brasileiro");
        dto.setEmail("joao.silva@example.com");

        Set<ConstraintViolation<CiclistaInPutDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    public void testInvalidCPF() {
        CiclistaInPutDTO dto = new CiclistaInPutDTO();
        dto.setNome("João Silva");
        dto.setNascimento("2000-01-01");
        dto.setCpf("invalid-cpf");
        dto.setNacionalidade("Brasileiro");
        dto.setEmail("joao.silva@example.com");

        Set<ConstraintViolation<CiclistaInPutDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("número do registro de contribuinte individual brasileiro (CPF) inválido", violations.iterator().next().getMessage());
    }

    @Test
    public void testBlankFields() {
        CiclistaInPutDTO dto = new CiclistaInPutDTO();
        dto.setNome("");
        dto.setNascimento("");
        dto.setCpf("");
        dto.setNacionalidade("");
        dto.setEmail("invalid-email");

        Set<ConstraintViolation<CiclistaInPutDTO>> violations = validator.validate(dto);

        assertEquals(5, violations.size()); // Considerando que todos os campos obrigatórios estão em branco
    }

}
