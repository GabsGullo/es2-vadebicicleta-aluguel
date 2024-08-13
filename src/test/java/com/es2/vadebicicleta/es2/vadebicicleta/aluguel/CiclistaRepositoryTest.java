package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CiclistaRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.IdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CiclistaRepositoryTest {

    @InjectMocks
    private CiclistaRepository ciclistaRepository;

    @Spy
    private IdGenerator idGenerator;

    @Test
    void testSave_NewCiclista() {
        Ciclista ciclista = new Ciclista();

        Ciclista savedCiclista = ciclistaRepository.save(ciclista);

        assertNotNull(savedCiclista.getId());
        assertEquals(1, savedCiclista.getId());

        Optional<Ciclista> retrievedCiclista = ciclistaRepository.findById(1);
        assertTrue(retrievedCiclista.isPresent());
        assertEquals(savedCiclista, retrievedCiclista.get());
    }

    @Test
    void testSave_UpdateExistingCiclista() {
        Ciclista ciclista = new Ciclista();
        ciclistaRepository.save(ciclista);

        ciclista.setNome("Nome Atualizado");

        ciclistaRepository.save(ciclista);

        Optional<Ciclista> retrievedCiclista = ciclistaRepository.findById(1);
        assertTrue(retrievedCiclista.isPresent());
        assertEquals("Nome Atualizado", retrievedCiclista.get().getNome());
    }

    @Test
    void testFindById_CiclistaExists() {
        Ciclista ciclista = new Ciclista();
        ciclistaRepository.save(ciclista);

        Optional<Ciclista> retrievedCiclista = ciclistaRepository.findById(1);

        assertTrue(retrievedCiclista.isPresent());
        assertEquals(ciclista, retrievedCiclista.get());
    }

    @Test
    void testFindById_CiclistaDoesNotExist() {
        Optional<Ciclista> retrievedCiclista = ciclistaRepository.findById(99);
        assertFalse(retrievedCiclista.isPresent());
    }
}
