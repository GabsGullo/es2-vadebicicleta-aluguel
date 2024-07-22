package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.NacionalidadeEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.StatusEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao.ExternoClient;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CiclistaRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CartaoDeCreditoService;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CiclistaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CiclistaServiceTest {

	@Mock
	private CiclistaRepository repository;

	@Mock
	private CartaoDeCreditoService cartaoDeCreditoService;

	@Mock
	private ExternoClient externoClient;

	@InjectMocks
	private CiclistaService ciclistaService;


	private Ciclista createCiclistaComPassaporte(Integer id, String nome, String nascimento,
												 String email, String urlFotoDocumento,
												 String senha, String numeroPassaporte, String validadePassaporte,
												 String paisPassaporte, NacionalidadeEnum nacionalidade) {

		Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
		passaporte.setNumero(numeroPassaporte);
		passaporte.setValidade(validadePassaporte);
		passaporte.setPais(paisPassaporte);

		return Ciclista.builder()
				.id(id)
				.nome(nome)
				.nascimento(nascimento)
				.email(email)
				.urlFotoDocumento(urlFotoDocumento)
				.senha(senha)
				.passaporte(passaporte)
				.nacionalidade(nacionalidade)
				.build();
	}

	private Ciclista createCiclistaSemPassaporte(Integer id, String nome, String nascimento,
									String cpf, String email, String urlFotoDocumento,
									String senha,NacionalidadeEnum nacionalidade) {

		return Ciclista.builder()
				.id(id)
				.nome(nome)
				.nascimento(nascimento)
				.cpf(cpf)
				.email(email)
				.urlFotoDocumento(urlFotoDocumento)
				.senha(senha)
				.nacionalidade(nacionalidade)
				.build();
	}

	private CartaoDeCredito createCartaoDeCredito(Integer id, String nomeTitular, String numero,
												  String validade, String cvv) {

		CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
		cartaoDeCredito.setId(id);
		cartaoDeCredito.setNomeTitular(nomeTitular);
		cartaoDeCredito.setNumero(numero);
		cartaoDeCredito.setValidade(validade);
		cartaoDeCredito.setCvv(cvv);
		return cartaoDeCredito;
	}

	@Test
	void testRegisterComPassaporte() {
		// Inicializando objetos dentro do método de teste
		Ciclista ciclista = createCiclistaComPassaporte(
				1, "Arrascaeta", "1990-01-01",
				"arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", "A1234567", "2025-12-31", "Brasil", NacionalidadeEnum.ESTRANGEIRO);

		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				"12/25", "123");

		// Configurando mocks
		when(repository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));
		doNothing().when(cartaoDeCreditoService).register(cartaoDeCredito);

		// Chamando o método a ser testado
		Ciclista result = ciclistaService.register(ciclista, cartaoDeCredito);

		// Verificando o resultado
		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals("Arrascaeta", result.getNome());
		assertEquals("1990-01-01", result.getNascimento());
		assertEquals("arrascaeta@flamengo.com", result.getEmail());
		assertEquals("http://exemplo.com/foto.jpg", result.getUrlFotoDocumento());
		assertEquals("password", result.getSenha());

		// Teste com passaporte
		assertNotNull(result.getPassaporte());
		assertEquals("A1234567", result.getPassaporte().getNumero());
		assertEquals("2025-12-31", result.getPassaporte().getValidade());
		assertEquals("Brasil", result.getPassaporte().getPais());

		verify(repository, times(1)).save(ciclista);
		verify(cartaoDeCreditoService, times(1)).register(cartaoDeCredito);
	}

	@Test
	void testRegisterSemPassaporte() {
		// Inicializando objetos dentro do método de teste
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", "1990-01-01",
				"17970421733", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				"12/25", "123");

		// Configurando mocks
		when(repository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));
		doNothing().when(cartaoDeCreditoService).register(cartaoDeCredito);

		// Chamando o método a ser testado
		Ciclista result = ciclistaService.register(ciclista, cartaoDeCredito);

		// Verificando o resultado
		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals(StatusEnum.AGUARDANDO_CONFIRMACAO, result.getStatus());
		assertEquals("Arrascaeta", result.getNome());
		assertEquals("1990-01-01", result.getNascimento());
		assertEquals("17970421733", result.getCpf());
		assertEquals("arrascaeta@flamengo.com", result.getEmail());
		assertEquals("http://exemplo.com/foto.jpg", result.getUrlFotoDocumento());
		assertEquals("password", result.getSenha());

		verify(repository, times(1)).save(ciclista);
		verify(cartaoDeCreditoService, times(1)).register(cartaoDeCredito);
	}

	@Test
	void testRegisterComPassaporteAtributosInvalidos() {
		// Inicializando objetos dentro do método de teste
		Ciclista ciclista = createCiclistaComPassaporte(
				1, null, null, null,
				null, null, null,
				null, null, null);
		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, null, null, null, null);

		// Testando o método a ser testado com uma exceção esperada
		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.register(ciclista, cartaoDeCredito);
		});

		// Verificando que o método repository.save não foi chamado
		verify(repository, never()).save(any(Ciclista.class));
	}

	@Test
	void testRegisterSemPassaporteAtributosInvalidos() {
		// Inicializando objetos dentro do método de teste
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, null, null, null,
				null, null, null,
				null);
		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, null, null, null, null);

		// Testando o método a ser testado com uma exceção esperada
		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.register(ciclista, cartaoDeCredito);
		});

		// Verificando que o método repository.save não foi chamado
		verify(repository, never()).save(any(Ciclista.class));
	}



}
