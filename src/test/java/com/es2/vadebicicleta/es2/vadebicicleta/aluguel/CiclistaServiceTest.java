package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.validator.Validator;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.*;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao.ExternoClient;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CiclistaRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CartaoDeCreditoService;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CiclistaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CiclistaServiceTest {
	@Mock
	private Validator validator;
	@Mock
	private CiclistaRepository ciclistaRepository;

	@Mock
	private CartaoDeCreditoService cartaoDeCreditoService;

	@Mock
	private ExternoClient externoClient;

	@InjectMocks
	private CiclistaService ciclistaService;

	private Ciclista existingCiclista;

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    private Ciclista createCiclistaComPassaporte(Integer id, String nome, LocalDate nascimento,
												 String email, String urlFotoDocumento,
												 String senha, String numeroPassaporte, LocalDate validadePassaporte,
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

	private Ciclista createCiclistaSemPassaporte(Integer id, String nome, LocalDate nascimento,
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
												  LocalDate validade, String cvv) {

		CartaoDeCredito cartaoDeCredito = new CartaoDeCredito();
		cartaoDeCredito.setId(id);
		cartaoDeCredito.setNomeTitular(nomeTitular);
		cartaoDeCredito.setNumero(numero);
		cartaoDeCredito.setValidade(validade);
		cartaoDeCredito.setCvv(cvv);
		return cartaoDeCredito;
	}

	@BeforeEach
	void setUp() {
		String nascimento = "1990-01-01";

        existingCiclista = Ciclista.builder()
                .id(1)
                .nome("Existing Name")
                .nascimento(LocalDate.parse(nascimento, dateTimeFormatter))
                .email("existingemail@example.com")
                .nacionalidade(NacionalidadeEnum.BRASILEIRO)
                .urlFotoDocumento("http://existingurl.com/photo.jpg")
				.senha("12345")
                .build();
	}

	@Test
	void testRegisterComPassaporte() {
		String nascimento = "1990-01-01";
		String validadePassaporte = "2025-12-31";
		// Inicializando objetos dentro do método de teste
		Ciclista ciclista = createCiclistaComPassaporte(
				1, "Arrascaeta", LocalDate.parse(nascimento, dateTimeFormatter),
				"arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", "A1234567", LocalDate.parse(validadePassaporte, dateTimeFormatter), "Brasil", NacionalidadeEnum.ESTRANGEIRO);

		String validade = "2025-12-01";
		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				LocalDate.parse(validade, dateTimeFormatter), "123");

		// Configurando mocks
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));
		doNothing().when(cartaoDeCreditoService).register(cartaoDeCredito);

		// Chamando o método a ser testado
		Ciclista result = ciclistaService.register(ciclista, cartaoDeCredito);

		// Verificando o resultado
		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals("Arrascaeta", result.getNome());
		assertEquals("1990-01-01", result.getNascimento().format(dateTimeFormatter));
		assertEquals("arrascaeta@flamengo.com", result.getEmail());
		assertEquals("http://exemplo.com/foto.jpg", result.getUrlFotoDocumento());
		assertEquals("password", result.getSenha());

		// Teste com passaporte
		assertNotNull(result.getPassaporte());
		assertEquals("A1234567", result.getPassaporte().getNumero());
		assertEquals("2025-12-31", result.getPassaporte().getValidade().format(dateTimeFormatter));
		assertEquals("Brasil", result.getPassaporte().getPais());

		verify(ciclistaRepository, times(1)).save(ciclista);
		verify(cartaoDeCreditoService, times(1)).register(cartaoDeCredito);
	}

	@Test
	void testRegisterSemPassaporte() {
		String nascimento = "1990-01-01";
		// Inicializando objetos dentro do método de teste
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", LocalDate.parse(nascimento, dateTimeFormatter),
				"17970421733", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		String validade = "2025-12-01";
		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				LocalDate.parse(validade, dateTimeFormatter), "123");

		// Configurando mocks
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));
		doNothing().when(cartaoDeCreditoService).register(cartaoDeCredito);

		// Chamando o método a ser testado
		Ciclista result = ciclistaService.register(ciclista, cartaoDeCredito);

		// Verificando o resultado
		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals(StatusEnum.AGUARDANDO_CONFIRMACAO, result.getStatus());
		assertEquals("Arrascaeta", result.getNome());
		assertEquals("1990-01-01", result.getNascimento().format(dateTimeFormatter));
		assertEquals("17970421733", result.getCpf());
		assertEquals("arrascaeta@flamengo.com", result.getEmail());
		assertEquals("http://exemplo.com/foto.jpg", result.getUrlFotoDocumento());
		assertEquals("password", result.getSenha());

		verify(ciclistaRepository, times(1)).save(ciclista);
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
		doThrow(ValidacaoException.class).when(validator).validateCiclista(any(Ciclista.class));

		// Testando o método a ser testado com uma exceção esperada
		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.register(ciclista, cartaoDeCredito);
		});

		// Verificando que o método repository.save não foi chamado
		verify(ciclistaRepository, never()).save(any(Ciclista.class));
	}

	@Test
	void testRegisterSemPassaporteAtributosInvalidos() {
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, null, null, null,
				null, null, null,
				null);
		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, null, null, null, null);
		doThrow(ValidacaoException.class).when(validator).validateCiclista(ciclista);

		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.register(ciclista, cartaoDeCredito);
		});

		verify(ciclistaRepository, never()).save(any(Ciclista.class));
	}

	@Test
	void testUpdateCiclistaComPassaporte() {
		String nascimento = "2002-10-14";
		String validadePassaporte = "2030-01-01";
		Ciclista ciclistaNovo = createCiclistaComPassaporte(1, "Rafael", LocalDate.parse(nascimento, dateTimeFormatter),
				"xqdl@gmail.com" ,"asdasdasd.jpg", "123", "987654321",
				LocalDate.parse(validadePassaporte, dateTimeFormatter), "Canada", NacionalidadeEnum.ESTRANGEIRO);

		when(ciclistaRepository.findById(existingCiclista.getId())).thenReturn(Optional.of(existingCiclista));
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Ciclista updatedCiclista = ciclistaService.update(ciclistaNovo, existingCiclista.getId());


		assertNotNull(updatedCiclista);
		assertEquals("Rafael", updatedCiclista.getNome());
		assertEquals("2002-10-14", updatedCiclista.getNascimento().format(dateTimeFormatter));
		assertEquals("xqdl@gmail.com", updatedCiclista.getEmail());
		assertEquals("123", updatedCiclista.getSenha());
		assertEquals(NacionalidadeEnum.ESTRANGEIRO, updatedCiclista.getNacionalidade());
		assertEquals("asdasdasd.jpg", updatedCiclista.getUrlFotoDocumento());

		assertNotNull(updatedCiclista.getPassaporte());
		assertEquals("987654321", updatedCiclista.getPassaporte().getNumero());
		assertEquals("2030-01-01", updatedCiclista.getPassaporte().getValidade().format(dateTimeFormatter));
		assertEquals("Canada", updatedCiclista.getPassaporte().getPais());

		verify(ciclistaRepository, times(1)).save(updatedCiclista);
	}

	@Test
	void testUpdateCiclistaComCPF() {
		String nascimento = "2002-10-14";
		Ciclista ciclistaNovo = createCiclistaSemPassaporte(1, "Rafael", LocalDate.parse(nascimento, dateTimeFormatter), "02001599099",
				"xqdl@gmail.com" ,"asdasdasd.jpg", "123", NacionalidadeEnum.BRASILEIRO);

		when(ciclistaRepository.findById(existingCiclista.getId())).thenReturn(Optional.of(existingCiclista));
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Ciclista updatedCiclista = ciclistaService.update(ciclistaNovo, existingCiclista.getId());

		assertNotNull(updatedCiclista);
		assertEquals("Rafael", updatedCiclista.getNome());
		assertEquals("2002-10-14", updatedCiclista.getNascimento().format(dateTimeFormatter));
		assertEquals("02001599099", updatedCiclista.getCpf());
		assertEquals("xqdl@gmail.com", updatedCiclista.getEmail());
		assertEquals("123", updatedCiclista.getSenha());
		assertEquals(NacionalidadeEnum.BRASILEIRO, updatedCiclista.getNacionalidade());
		assertEquals("asdasdasd.jpg", updatedCiclista.getUrlFotoDocumento());

		verify(ciclistaRepository, times(1)).save(updatedCiclista);
	}

	@Test
	void testUpdateCiclistaComPassaporteSemCPFParaBrasileiro() {
		String nascimento = "2002-10-14";
		String validadePassaporte = "2024-06-01";
		Ciclista ciclistaNovo = createCiclistaComPassaporte(1, "Rafael", LocalDate.parse(nascimento, dateTimeFormatter),
				"xqdl@gmail.com" ,"asdasdasd.jpg", "123", "2223322", LocalDate.parse(validadePassaporte, dateTimeFormatter),
				"EUA", NacionalidadeEnum.BRASILEIRO);

		Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
		passaporte.setNumero("2223322");
		passaporte.setValidade(LocalDate.parse(validadePassaporte, dateTimeFormatter));
		passaporte.setPais("EUA");
		ciclistaNovo.setPassaporte(passaporte);

		when(ciclistaRepository.findById(existingCiclista.getId())).thenReturn(Optional.of(existingCiclista));
		doThrow(ValidacaoException.class).when(validator).validateCiclista(any(Ciclista.class));

		Integer ciclistaId = existingCiclista.getId();
		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.update(ciclistaNovo, ciclistaId);
		});

		verify(ciclistaRepository, never()).save(any(Ciclista.class));
	}

	@Test
	void testUpdateCiclistaComCPFAndPassaporte() {
		String nascimento = "2002-10-14";
		String validadePassaporte = "2024-06-01";
		Ciclista ciclistaNovo = createCiclistaSemPassaporte(1, "Rafael", LocalDate.parse(nascimento, dateTimeFormatter), "02001599099",
				"xqdl@gmail.com" ,"asdasdasd.jpg", "123", NacionalidadeEnum.BRASILEIRO);

		Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
		passaporte.setNumero("2223322");
		passaporte.setValidade(LocalDate.parse(validadePassaporte, dateTimeFormatter));
		passaporte.setPais("EUA");
		ciclistaNovo.setPassaporte(passaporte);

		when(ciclistaRepository.findById(existingCiclista.getId())).thenReturn(Optional.of(existingCiclista));
		doThrow(ValidacaoException.class).when(validator).validateCiclista(any(Ciclista.class));

		Integer ciclistaId = existingCiclista.getId();
		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.update(ciclistaNovo, ciclistaId);
		});

		verify(ciclistaRepository, never()).save(any(Ciclista.class));
	}

	@Test
	void testUpdateCiclistaComCPFSemPassaporteParaEstrangeiro() {
		String nascimento = "2002-10-14";
		Ciclista ciclistaNovo = createCiclistaSemPassaporte(1, "Rafael", LocalDate.parse(nascimento, dateTimeFormatter), "02001599099",
				"xqdl@gmail.com" ,"asdasdasd.jpg", "123", NacionalidadeEnum.ESTRANGEIRO);

		when(ciclistaRepository.findById(existingCiclista.getId())).thenReturn(Optional.of(existingCiclista));
		doThrow(ValidacaoException.class).when(validator).validateCiclista(any(Ciclista.class));

		Integer ciclistaId = existingCiclista.getId();
		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.update(ciclistaNovo, ciclistaId);
		});

		verify(ciclistaRepository, never()).save(any(Ciclista.class));
	}

	@Test
	void testNotfindCiclistaById(){

		assertThrows(NotFoundException.class, () -> {
			ciclistaService.getById(1);
		});

	}

	@Test
	void testActivateCiclista(){
		String nascimento = "1990-01-01";
		// Criando Ciclista e Cartão de Crédito
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", LocalDate.parse(nascimento, dateTimeFormatter),
				"17970421733", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		String validade = "2025-12-01";
		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				LocalDate.parse(validade, dateTimeFormatter), "123");

		// Configurando mocks
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(ciclistaRepository.findById(ciclista.getId())).thenReturn(Optional.of(ciclista));
		doNothing().when(cartaoDeCreditoService).register(cartaoDeCredito);

		// Chamando o método a ser testado
		ciclistaService.register(ciclista, cartaoDeCredito);
		Ciclista result = ciclistaService.activate(ciclista.getId());

		// Verificações
		assertEquals(StatusEnum.ATIVO, result.getStatus());
		verify(cartaoDeCreditoService, times(1)).register(cartaoDeCredito);
		verify(ciclistaRepository, times(2)).save(any(Ciclista.class));
	}

	@Test
	void testRegisterWithValidCPF() {
		// Configurando os dados de entrada
		String nascimento = "1990-01-01";
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", LocalDate.parse(nascimento, dateTimeFormatter),
				"12345678909", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		String validade = "2025-12-01";
		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				LocalDate.parse(validade, dateTimeFormatter), "123");

		// Configurando mocks
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));
		doNothing().when(cartaoDeCreditoService).register(cartaoDeCredito);

		// Chamando o método a ser testado
		Ciclista result = ciclistaService.register(ciclista, cartaoDeCredito);

		// Verificação
		assertTrue(result != null && result.getCpf().equals(ciclista.getCpf()), "Ciclista deveria ser registrado com CPF válido");
		verify(cartaoDeCreditoService, times(1)).register(cartaoDeCredito);
		verify(ciclistaRepository, times(1)).save(any(Ciclista.class));
	}

	@Test
	void testRegisterWithInvalidCPF() {
		// Configurando os dados de entrada
		String nascimento = "1990-01-01";
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", LocalDate.parse(nascimento, dateTimeFormatter),
				"12345678900", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		String validade = "2025-12-01";
		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				LocalDate.parse(validade, dateTimeFormatter), "123");

		doThrow(ValidacaoException.class).when(validator).validateCiclista(any(Ciclista.class));

		// Verificação e exceção esperada
		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.register(ciclista, cartaoDeCredito);
		}, "CPF inválido deveria lançar uma exceção");

		verify(cartaoDeCreditoService, never()).register(any(CartaoDeCredito.class));
		verify(ciclistaRepository, never()).save(any(Ciclista.class));
	}

	@Test
	void testRegisterWithNullCPF() {
		String nascimento = "1990-01-01";
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", LocalDate.parse(nascimento, dateTimeFormatter),
				null, "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		String validade = "2025-12-01";
		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				LocalDate.parse(validade, dateTimeFormatter), "123");

		doThrow(ValidacaoException.class).when(validator).validateCiclista(any(Ciclista.class));

		// Verificação e exceção esperada
		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.register(ciclista, cartaoDeCredito);
		}, "CPF nulo deveria lançar uma exceção");

		verify(cartaoDeCreditoService, never()).register(any(CartaoDeCredito.class));
		verify(ciclistaRepository, never()).save(any(Ciclista.class));
	}

	@Test
	void testRegisterWithPontuacaoCPF() {
		// Configurando os dados de entrada
		String nascimento = "1990-01-01";
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta",LocalDate.parse(nascimento, dateTimeFormatter),
				"123.456.789-09", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		String validade = "2025-12-01";
		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				LocalDate.parse(validade, dateTimeFormatter), "123");

		// Configurando mocks
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));
		doNothing().when(cartaoDeCreditoService).register(cartaoDeCredito);

		// Chamando o método a ser testado
		Ciclista result = ciclistaService.register(ciclista, cartaoDeCredito);

		// Verificação
		assertTrue(result != null && result.getCpf().equals("123.456.789-09"), "Ciclista deveria ser registrado com CPF válido e formatado");
		verify(cartaoDeCreditoService, times(1)).register(cartaoDeCredito);
		verify(ciclistaRepository, times(1)).save(any(Ciclista.class));
	}

}
