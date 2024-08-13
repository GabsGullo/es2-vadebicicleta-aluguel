package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.FuncionarioRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.IdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioRepositoryTest {

    @InjectMocks
    private FuncionarioRepository funcionarioRepository;

    @Spy
    private IdGenerator idGenerator;

    @Spy
    private IdGenerator matriculaGenerator;

    @Test
    void testSave_NewFuncionario() {
        Funcionario funcionario = new Funcionario();

        Funcionario savedFuncionario = funcionarioRepository.save(funcionario);

        assertNotNull(savedFuncionario.getId());
        assertNotNull(savedFuncionario.getMatricula());
        assertEquals("1", savedFuncionario.getMatricula());
        assertEquals(1, savedFuncionario.getId());

        Optional<Funcionario> retrievedFuncionario = funcionarioRepository.findById(1);
        assertTrue(retrievedFuncionario.isPresent());
        assertEquals(savedFuncionario, retrievedFuncionario.get());
    }

    @Test
    void testFindById_FuncionarioExists() {
        Funcionario funcionario = new Funcionario();
        funcionarioRepository.save(funcionario);

        Optional<Funcionario> retrievedFuncionario = funcionarioRepository.findById(1);
        assertTrue(retrievedFuncionario.isPresent());
        assertEquals(funcionario, retrievedFuncionario.get());
    }

    @Test
    void testSave_UpdateExistingFuncionario() {

        Funcionario funcionario = new Funcionario();
        funcionarioRepository.save(funcionario);

        Funcionario updatedFuncionario = new Funcionario();
        updatedFuncionario.setId(1);
        updatedFuncionario.setMatricula("MAT-002");

        Funcionario savedFuncionario = funcionarioRepository.save(updatedFuncionario);

        assertEquals("MAT-002", savedFuncionario.getMatricula());

        Optional<Funcionario> retrievedFuncionario = funcionarioRepository.findById(1);
        assertTrue(retrievedFuncionario.isPresent());
        assertEquals(savedFuncionario, retrievedFuncionario.get());
    }

    @Test
    void testFindById_FuncionarioDoesNotExist() {
        Optional<Funcionario> retrievedFuncionario = funcionarioRepository.findById(99);
        assertFalse(retrievedFuncionario.isPresent());
    }

    @Test
    void testGetAllFuncionarios() {
        Funcionario funcionario1 = new Funcionario();
        Funcionario funcionario2 = new Funcionario();

        funcionarioRepository.save(funcionario1);
        funcionarioRepository.save(funcionario2);

        List<Funcionario> funcionarios = funcionarioRepository.getAllFuncionarios();

        assertEquals(2, funcionarios.size());
        assertTrue(funcionarios.contains(funcionario1));
        assertTrue(funcionarios.contains(funcionario2));
    }

    @Test
    void testDelete_FuncionarioExists() {
        Funcionario funcionario = new Funcionario();
        funcionarioRepository.save(funcionario);

        Object deletedFuncionario = funcionarioRepository.delete(1);
        assertNotNull(deletedFuncionario);

        Optional<Funcionario> retrievedFuncionario = funcionarioRepository.findById(1);
        assertFalse(retrievedFuncionario.isPresent());
    }

    @Test
    void testDelete_FuncionarioDoesNotExist() {
        Object deletedFuncionario = funcionarioRepository.delete(99);
        assertNull(deletedFuncionario);
    }
}
