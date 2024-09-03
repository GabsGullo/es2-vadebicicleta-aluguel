package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.CiclistaConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.NacionalidadeEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPostDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaOutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class CiclistaConverterTest {

    private CiclistaConverter ciclistaConverter;

    @BeforeEach
    void setUp() {
        ciclistaConverter = new CiclistaConverter();
    }

    @Test
    void testEntityToOutDTO() {
        // Arrange
        Ciclista.Passaporte passaporte = new Ciclista.Passaporte("A12345678", LocalDate.of(2030, 12, 31), "EUA");

        Ciclista ciclista = Ciclista.builder()
                .id(1)
                .nome("João")
                .nascimento(LocalDate.of(1990, 1, 1))
                .cpf("12345678900")
                .passaporte(passaporte)
                .nacionalidade(NacionalidadeEnum.ESTRANGEIRO)
                .email("joao@example.com")
                .urlFotoDocumento("urlFoto")
                .build();

        // Act
        CiclistaOutDTO dto = ciclistaConverter.entityToOutDTO(ciclista);

        // Assert
        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("João", dto.getNome());
        assertEquals("1990-01-01", dto.getNascimento());
        assertEquals("12345678900", dto.getCpf());

        // Verificando os atributos do passaporte
        String validade = "2030-12-31";
        assertNotNull(dto.getPassaporte());
        assertEquals("A12345678", dto.getPassaporte().getNumero());
        assertEquals(LocalDate.parse(validade, DateTimeFormatter.ISO_LOCAL_DATE), dto.getPassaporte().getValidade());
        assertEquals("EUA", dto.getPassaporte().getPais());

        assertEquals("ESTRANGEIRO", dto.getNacionalidade());
        assertEquals("joao@example.com", dto.getEmail());
        assertEquals("urlFoto", dto.getUrlFotoDocumento());
    }


    @Test
    void testInPutDtoToEntity() {
        // Arrange
        Ciclista.Passaporte passaporte = new Ciclista.Passaporte("A12345678", LocalDate.of(2030, 12, 31), "EUA");
        CiclistaInPutDTO dto = new CiclistaInPutDTO();

        dto.setNome("Maria");
        dto.setNascimento("1995-05-15");
        dto.setCpf("98765432100");
        dto.setPassaporte(passaporte);
        dto.setNacionalidade("BRASILEIRO");
        dto.setEmail("maria@example.com");
        dto.setUrlFotoDocumento("urlFotoMaria");

        // Act
        Ciclista ciclista = ciclistaConverter.inPutDtoToEntity(dto);

        // Assert
        assertNotNull(ciclista);
        assertEquals("Maria", ciclista.getNome());
        assertEquals(LocalDate.of(1995, 5, 15), ciclista.getNascimento());
        assertEquals("98765432100", ciclista.getCpf());
        String validade = "2030-12-31";
        assertNotNull(dto.getPassaporte());
        assertEquals("A12345678", dto.getPassaporte().getNumero());
        assertEquals(LocalDate.parse(validade, DateTimeFormatter.ISO_LOCAL_DATE), dto.getPassaporte().getValidade());
        assertEquals("EUA", dto.getPassaporte().getPais());
        assertEquals(NacionalidadeEnum.BRASILEIRO, ciclista.getNacionalidade());
        assertEquals("maria@example.com", ciclista.getEmail());
        assertEquals("urlFotoMaria", ciclista.getUrlFotoDocumento());
    }

    @Test
    void testInPostDtoToEntity() {
        // Arrange
        Ciclista.Passaporte passaporte = new Ciclista.Passaporte("A12345678", LocalDate.of(2030, 12, 31), "EUA");

        CiclistaInPostDTO dto = new CiclistaInPostDTO();
        dto.setNome("Carlos");
        dto.setNascimento("1985-10-20");
        dto.setCpf("12312312300");
        dto.setPassaporte(passaporte);
        dto.setNacionalidade("ESTRANGEIRO");
        dto.setEmail("carlos@example.com");
        dto.setUrlFotoDocumento("urlFotoCarlos");
        dto.setSenha("senhaSegura");

        // Act
        Ciclista ciclista = ciclistaConverter.inPostDtoToEntity(dto);

        // Assert
        assertNotNull(ciclista);
        assertEquals("Carlos", ciclista.getNome());
        assertEquals(LocalDate.of(1985, 10, 20), ciclista.getNascimento());
        assertEquals("12312312300", ciclista.getCpf());

        // Verificando os atributos do passaporte
        assertNotNull(ciclista.getPassaporte());
        assertEquals("A12345678", ciclista.getPassaporte().getNumero());
        assertEquals(LocalDate.of(2030, 12, 31), ciclista.getPassaporte().getValidade());
        assertEquals("EUA", ciclista.getPassaporte().getPais());

        assertEquals(NacionalidadeEnum.ESTRANGEIRO, ciclista.getNacionalidade());
        assertEquals("carlos@example.com", ciclista.getEmail());
        assertEquals("urlFotoCarlos", ciclista.getUrlFotoDocumento());
        assertEquals("senhaSegura", ciclista.getSenha());
    }
}
