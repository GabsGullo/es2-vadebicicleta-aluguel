package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.AluguelRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.IdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AluguelRepositoryTest {

    @InjectMocks
    private AluguelRepository aluguelRepository;

    @Spy
    private IdGenerator idGenerator;

    @Test
    void testRegisterNewAluguel() {
        // Criação do objeto aluguel
        Aluguel aluguel = Aluguel.builder()
                .idAluguel(1)
                .bicicleta(101)
                .horaInicio(LocalDateTime.now())
                .horaFim(LocalDateTime.now().plusHours(1))
                .ciclista(1001)
                .build();

        // Salva o objeto aluguel no repositório
        Aluguel savedAluguel = aluguelRepository.register(aluguel);

        // Verifica se o objeto salvo não é nulo
        assertNotNull(savedAluguel);

        // Verifica se os valores salvos são iguais aos valores esperados
        assertEquals(aluguel.getIdAluguel(), savedAluguel.getIdAluguel());
        assertEquals(aluguel.getBicicleta(), savedAluguel.getBicicleta());
        assertEquals(aluguel.getHoraInicio(), savedAluguel.getHoraInicio());
        assertEquals(aluguel.getHoraFim(), savedAluguel.getHoraFim());
        assertEquals(aluguel.getCiclista(), savedAluguel.getCiclista());
    }

    @Test
    void testRegisterExistingAluguel() {
        // Configura um aluguel inicial
        Aluguel aluguelInicial = Aluguel.builder()
                .idAluguel(1)
                .bicicleta(101)
                .horaInicio(LocalDateTime.now())
                .horaFim(LocalDateTime.now().plusHours(1))
                .ciclista(1001)
                .build();

        // Registra o aluguel inicial no repositório
        aluguelRepository.register(aluguelInicial);

        // Cria um novo objeto de aluguel com os mesmos ID e dados atualizados
        Aluguel aluguelAtualizado = Aluguel.builder()
                .idAluguel(1)  // Mesmo ID
                .bicicleta(102)  // Dados atualizados
                .horaInicio(LocalDateTime.now())
                .horaFim(LocalDateTime.now().plusHours(2))
                .ciclista(1002)
                .build();

        // Registra o aluguel atualizado no repositório
        Aluguel updatedAluguel = aluguelRepository.register(aluguelAtualizado);

        // Verifica se o aluguel foi atualizado corretamente
        assertNotNull(updatedAluguel);
        assertEquals(102, updatedAluguel.getBicicleta());
        assertEquals(1002, updatedAluguel.getCiclista());
    }

    @Test
    void testFindById() {
        // Cria um novo aluguel
        Aluguel aluguel = Aluguel.builder()
                .idAluguel(1)
                .bicicleta(101)
                .horaInicio(LocalDateTime.now())
                .horaFim(LocalDateTime.now().plusHours(1))
                .ciclista(1001)
                .build();

        // Registra o aluguel no repositório
        aluguelRepository.register(aluguel);

        // Usa o método real para procurar o aluguel
        Optional<Aluguel> foundAluguel = aluguelRepository.findById(1);

        // Verifica se o aluguel foi encontrado
        assertTrue(foundAluguel.isPresent());

        // Compara os campos do aluguel encontrado com o esperado
        Aluguel found = foundAluguel.get();
        assertEquals(aluguel.getIdAluguel(), found.getIdAluguel());
        assertEquals(aluguel.getBicicleta(), found.getBicicleta());
        assertEquals(aluguel.getHoraInicio(), found.getHoraInicio());
        assertEquals(aluguel.getHoraFim(), found.getHoraFim());
        assertEquals(aluguel.getCiclista(), found.getCiclista());
    }

    @Test
    void testFindByBicicletaIdHoraFimAluguel() {
        // Cria um novo aluguel
        Aluguel aluguel = Aluguel.builder()
                .idAluguel(1)
                .bicicleta(101)
                .horaInicio(LocalDateTime.now())
                .horaFim(LocalDateTime.now().plusHours(1))
                .ciclista(1001)
                .build();

        // Registra o aluguel no repositório
        aluguelRepository.register(aluguel);

        // Usa o método real para procurar o aluguel
        Optional<Aluguel> foundAluguel = aluguelRepository.findByBicicletaIdHoraFimAluguel(aluguel.getBicicleta(), aluguel.getHoraFim());

        // Verifica se o aluguel foi encontrado
        assertTrue(foundAluguel.isPresent());

        // Compara os campos individuais
        Aluguel found = foundAluguel.get();
        assertEquals(aluguel.getIdAluguel(), found.getIdAluguel());
        assertEquals(aluguel.getBicicleta(), found.getBicicleta());
        assertEquals(aluguel.getHoraInicio(), found.getHoraInicio());
        assertEquals(aluguel.getHoraFim(), found.getHoraFim());
        assertEquals(aluguel.getCiclista(), found.getCiclista());
    }



    @Test
    void testFindByCiclistaIdHoraFimAluguel() {
        Aluguel aluguel = Aluguel.builder()
                .idAluguel(1)
                .bicicleta(101)
                .horaInicio(LocalDateTime.now())
                .horaFim(null)
                .ciclista(1)
                .build();

        aluguelRepository.register(aluguel);

        Optional<Aluguel> foundAluguel = aluguelRepository.findByCiclistaIdHoraFimAluguel(1, aluguel.getHoraFim());

        // Verifica se o aluguel foi encontrado
        assertTrue(foundAluguel.isPresent());

        // Compara os campos individuais
        Aluguel found = foundAluguel.get();
        assertEquals(aluguel.getIdAluguel(), found.getIdAluguel());
        assertEquals(aluguel.getBicicleta(), found.getBicicleta());
        assertEquals(aluguel.getHoraInicio(), found.getHoraInicio());
        assertEquals(aluguel.getHoraFim(), found.getHoraFim()); // Aqui pode ser null, então ajuste conforme necessário
        assertEquals(aluguel.getCiclista(), found.getCiclista());
    }

}

