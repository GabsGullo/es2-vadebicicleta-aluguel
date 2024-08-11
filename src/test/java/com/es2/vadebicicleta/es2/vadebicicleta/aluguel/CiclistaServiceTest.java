package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.*;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.UnprocessableEntityException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao.ExternoClient;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CiclistaRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.FuncionarioRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CartaoDeCreditoService;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CiclistaService;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.FuncionarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CiclistaServiceTest {

	@Mock
	private CiclistaRepository ciclistaRepository;

	@Mock
	private FuncionarioRepository funcionarioRepository;

	@Mock
	private CartaoDeCreditoService cartaoDeCreditoService;

	@Mock
	private ExternoClient externoClient;

	@InjectMocks
	private CiclistaService ciclistaService;

	@InjectMocks
	private FuncionarioService funcionarioService;

	private Ciclista existingCiclista;


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

	private Funcionario createFuncionario(Integer id, String matricula, String senha, String confirmacaoSenha, String email,
										  String nome, Integer idade, FuncaoEnum funcao, String cpf){

		return Funcionario.builder()
				.id(id)
				.matricula(matricula)
				.senha(senha)
				.confirmacaoSenha(confirmacaoSenha)
				.email(email)
				.nome(nome)
				.idade(idade)
				.funcao(funcao)
				.cpf(cpf)
				.build();
	}

	@BeforeEach
	void setUp() {
        existingCiclista = Ciclista.builder()
                .id(1)
                .nome("Existing Name")
                .nascimento("1990-01-01")
                .email("existingemail@example.com")
                .nacionalidade(NacionalidadeEnum.BRASILEIRO)
                .urlFotoDocumento("http://existingurl.com/photo.jpg")
				.senha("12345")
                .build();
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
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));
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

		verify(ciclistaRepository, times(1)).save(ciclista);
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
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));
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

		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.register(ciclista, cartaoDeCredito);
		});

		verify(ciclistaRepository, never()).save(any(Ciclista.class));
	}

	@Test
	void testUpdateCiclistaComPassaporte() {

		CiclistaInPutDTO ciclistaNovo = new CiclistaInPutDTO();
		ciclistaNovo.setNome("Alex");
		ciclistaNovo.setNascimento("1995-05-05");
		ciclistaNovo.setEmail("alex@foreigner.com");
		ciclistaNovo.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO.name());
		ciclistaNovo.setUrlFotoDocumento("alex-foreigner-photo.jpg");

		Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
		passaporte.setNumero("987654321");
		passaporte.setValidade("2030-01-01");
		passaporte.setPais("Canada");
		ciclistaNovo.setPassaporte(passaporte);

		when(ciclistaRepository.findById(existingCiclista.getId())).thenReturn(Optional.of(existingCiclista));
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Ciclista updatedCiclista = ciclistaService.update(ciclistaNovo, existingCiclista.getId());

		assertNotNull(updatedCiclista);
		assertEquals("Alex", updatedCiclista.getNome());
		assertEquals("1995-05-05", updatedCiclista.getNascimento());
		assertEquals("alex@foreigner.com", updatedCiclista.getEmail());
		assertEquals(NacionalidadeEnum.ESTRANGEIRO, updatedCiclista.getNacionalidade());
		assertEquals("alex-foreigner-photo.jpg", updatedCiclista.getUrlFotoDocumento());

		assertNotNull(updatedCiclista.getPassaporte());
		assertEquals("987654321", updatedCiclista.getPassaporte().getNumero());
		assertEquals("2030-01-01", updatedCiclista.getPassaporte().getValidade());
		assertEquals("Canada", updatedCiclista.getPassaporte().getPais());

		verify(ciclistaRepository, times(1)).save(updatedCiclista);
	}

	@Test
	void testUpdateCiclistaComCPF() {

		CiclistaInPutDTO ciclistaNovo = new CiclistaInPutDTO();
		ciclistaNovo.setNome("Lucas");
		ciclistaNovo.setNascimento("2002-10-14");
		ciclistaNovo.setCpf("02001599099");
		ciclistaNovo.setEmail("lucas@brasil.com");
		ciclistaNovo.setNacionalidade(NacionalidadeEnum.BRASILEIRO.name());
		ciclistaNovo.setUrlFotoDocumento("lucas-brasil-photo.jpg");

		when(ciclistaRepository.findById(existingCiclista.getId())).thenReturn(Optional.of(existingCiclista));
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Ciclista updatedCiclista = ciclistaService.update(ciclistaNovo, existingCiclista.getId());

		assertNotNull(updatedCiclista);
		assertEquals("Lucas", updatedCiclista.getNome());
		assertEquals("2002-10-14", updatedCiclista.getNascimento());
		assertEquals("02001599099", updatedCiclista.getCpf());
		assertEquals("lucas@brasil.com", updatedCiclista.getEmail());
		assertEquals(NacionalidadeEnum.BRASILEIRO, updatedCiclista.getNacionalidade());
		assertEquals("lucas-brasil-photo.jpg", updatedCiclista.getUrlFotoDocumento());

		verify(ciclistaRepository, times(1)).save(updatedCiclista);
	}

	@Test
	void testUpdateCiclistaComPassaporteSemCPFParaBrasileiro() {

		CiclistaInPutDTO ciclistaNovo = new CiclistaInPutDTO();
		ciclistaNovo.setNome("Lucas");
		ciclistaNovo.setNascimento("2002-10-14");
		ciclistaNovo.setEmail("xqdl@gmail.com");
		ciclistaNovo.setNacionalidade(NacionalidadeEnum.BRASILEIRO.name());
		ciclistaNovo.setUrlFotoDocumento("asdasdasd.jpg");

		Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
		passaporte.setNumero("2223322");
		passaporte.setValidade("2024-06-01");
		passaporte.setPais("EUA");
		ciclistaNovo.setPassaporte(passaporte);

		when(ciclistaRepository.findById(existingCiclista.getId())).thenReturn(Optional.of(existingCiclista));

		Integer ciclistaId = existingCiclista.getId();
		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.update(ciclistaNovo, ciclistaId);
		});

		verify(ciclistaRepository, never()).save(any(Ciclista.class));
	}

	@Test
	void testUpdateCiclistaComCPFAndPassaporte() {

		CiclistaInPutDTO ciclistaNovo = new CiclistaInPutDTO();
		ciclistaNovo.setNome("Rafael");
		ciclistaNovo.setNascimento("2002-10-14");
		ciclistaNovo.setCpf("02001599099");
		ciclistaNovo.setEmail("xqdl@gmail.com");
		ciclistaNovo.setNacionalidade(NacionalidadeEnum.BRASILEIRO.name());
		ciclistaNovo.setUrlFotoDocumento("asdasdasd.jpg");

		Ciclista.Passaporte passaporte = new Ciclista.Passaporte();
		passaporte.setNumero("2223322");
		passaporte.setValidade("2024-06-01");
		passaporte.setPais("EUA");
		ciclistaNovo.setPassaporte(passaporte);

		when(ciclistaRepository.findById(existingCiclista.getId())).thenReturn(Optional.of(existingCiclista));

		Integer ciclistaId = existingCiclista.getId();
		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.update(ciclistaNovo, ciclistaId);
		});

		verify(ciclistaRepository, never()).save(any(Ciclista.class));
	}

	@Test
	void testUpdateCiclistaComCPFSemPassaporteParaEstrangeiro() {

		CiclistaInPutDTO ciclistaNovo = new CiclistaInPutDTO();
		ciclistaNovo.setNome("Carlos");
		ciclistaNovo.setNascimento("1998-08-08");
		ciclistaNovo.setCpf("17970421733");
		ciclistaNovo.setEmail("carlos@foreign.com");
		ciclistaNovo.setNacionalidade(NacionalidadeEnum.ESTRANGEIRO.name());
		ciclistaNovo.setUrlFotoDocumento("carlos-foreign-photo.jpg");

		when(ciclistaRepository.findById(existingCiclista.getId())).thenReturn(Optional.of(existingCiclista));

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

		// Criando Ciclista e Cartão de Crédito
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", "1990-01-01",
				"17970421733", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				"12/25", "123");

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
	void testAlterarStatusAluguelTrueCiclista(){

		// Criando Ciclista e Cartão de Crédito
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", "1990-01-01",
				"17970421733", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				"12/25", "123");

		// Configurando mocks
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(ciclistaRepository.findById(ciclista.getId())).thenReturn(Optional.of(ciclista));
		doNothing().when(cartaoDeCreditoService).register(cartaoDeCredito);

		// Chamando o método a ser testado
		ciclistaService.register(ciclista, cartaoDeCredito);
		Ciclista result = ciclistaService.alterarStatusAluguel(ciclista.getId());

		// Verificações
		assertEquals(Boolean.TRUE, result.getAluguelAtivo());
		verify(cartaoDeCreditoService, times(1)).register(cartaoDeCredito);
		verify(ciclistaRepository, times(2)).save(any(Ciclista.class));
	}

	@Test
	void testAlterarStatusAluguelFalseCiclista(){

		// Criando Ciclista e Cartão de Crédito
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", "1990-01-01",
				"17970421733", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				"12/25", "123");

		// Configurando mocks
		when(ciclistaRepository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(ciclistaRepository.findById(ciclista.getId())).thenReturn(Optional.of(ciclista));
		doNothing().when(cartaoDeCreditoService).register(cartaoDeCredito);

		// Chamando o método a ser testado
		ciclistaService.register(ciclista, cartaoDeCredito);
		ciclistaService.alterarStatusAluguel(ciclista.getId());
		Ciclista result = ciclistaService.alterarStatusAluguel(ciclista.getId());

		// Verificações
		assertEquals(Boolean.FALSE, result.getAluguelAtivo());
		verify(cartaoDeCreditoService, times(1)).register(cartaoDeCredito);
		verify(ciclistaRepository, times(3)).save(any(Ciclista.class));
	}

	@Test
	void testRegisterWithValidCPF() {
		// Configurando os dados de entrada
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", "1990-01-01",
				"12345678909", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				"12/25", "123");

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
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", "1990-01-01",
				"12345678900", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				"12/25", "123");

		// Verificação e exceção esperada
		assertThrows(ValidacaoException.class, () -> {
			ciclistaService.register(ciclista, cartaoDeCredito);
		}, "CPF inválido deveria lançar uma exceção");

		verify(cartaoDeCreditoService, never()).register(any(CartaoDeCredito.class));
		verify(ciclistaRepository, never()).save(any(Ciclista.class));
	}

	@Test
	void testRegisterWithNullCPF() {
		// Configurando os dados de entrada
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", "1990-01-01",
				null, "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				"12/25", "123");

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
		Ciclista ciclista = createCiclistaSemPassaporte(
				1, "Arrascaeta", "1990-01-01",
				"123.456.789-09", "arrascaeta@flamengo.com", "http://exemplo.com/foto.jpg",
				"password", NacionalidadeEnum.BRASILEIRO);

		CartaoDeCredito cartaoDeCredito = createCartaoDeCredito(1, "Arrascaeta", "1234567812345678",
				"12/25", "123");

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

	@Test
	void testSaveFuncionario() {

		Funcionario funcionario = createFuncionario(1, "1", "123", "123", "arrascaeta@flamengo.com", "Arrascaeta",
				28, FuncaoEnum.REPARADOR, "123.456.789-09");

		when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Funcionario result = funcionarioService.save(funcionario);

		assertEquals(funcionario.getId(), result.getId());
		assertEquals(funcionario.getMatricula(), result.getMatricula());
		assertEquals(funcionario.getConfirmacaoSenha(), result.getConfirmacaoSenha());
		assertEquals(funcionario.getSenha(), result.getSenha());
		assertEquals(funcionario.getEmail(), result.getEmail());
		assertEquals(funcionario.getNome(), result.getNome());
		assertEquals(funcionario.getIdade(), result.getIdade());
		assertEquals(funcionario.getFuncao(), result.getFuncao());
		assertEquals(funcionario.getCpf(), result.getCpf());

		verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
	}

	@Test
	void testSaveFuncionarioTodosCamposNulos() {
		// Cria um Funcionario com todos os campos nulos ou inválidos
		Funcionario funcionario = createFuncionario(
				1,
				null,                // Matricula nula
				null,                // Senha nula
				null,                // ConfirmacaoSenha nula
				null,                // Email nulo
				null,                // Nome nulo
				null,                // Idade nula
				null,                // Funcao nula
				null                 // CPF nulo
		);

		// Espera uma exceção de validação
		assertThrows(ValidacaoException.class, () -> funcionarioService.save(funcionario));
	}

	@Test
	void testSaveFuncionarioComConfirmacaoSenhaDiferente() {
		Funcionario funcionario = createFuncionario(1, "1", "123", "456", "arrascaeta@flamengo.com", "Arrascaeta",
				28, FuncaoEnum.REPARADOR, "123.456.789-09");

		// Espera uma exceção de validação
		assertThrows(ValidacaoException.class, () -> funcionarioService.save(funcionario));
	}

	@Test
	void testNotfindFuncionarioById(){

		assertThrows(NotFoundException.class, () -> {
			funcionarioService.getById(1);
		});
	}

	@Test
	void testFindFuncionarioById() {
		Funcionario funcionario = createFuncionario(1, "1", "123", "123", "arrascaeta@flamengo.com", "Arrascaeta",
				28, FuncaoEnum.REPARADOR, "123.456.789-09");

		when(funcionarioRepository.findById(funcionario.getId())).thenReturn(Optional.of(funcionario));

		Funcionario result = funcionarioService.getById(1);

		assertEquals(funcionario.getId(), result.getId());
		assertEquals(funcionario.getMatricula(), result.getMatricula());
		assertEquals(funcionario.getConfirmacaoSenha(), result.getConfirmacaoSenha());
		assertEquals(funcionario.getSenha(), result.getSenha());
		assertEquals(funcionario.getEmail(), result.getEmail());
		assertEquals(funcionario.getNome(), result.getNome());
		assertEquals(funcionario.getIdade(), result.getIdade());
		assertEquals(funcionario.getFuncao(), result.getFuncao());
		assertEquals(funcionario.getCpf(), result.getCpf());
	}

	@Test
	void testUpdateFuncionario() {
		Funcionario funcionarioNovo = createFuncionario(
				1,"1","novaSenha",
				"novaSenha",
				"novoEmail@exemplo.com",
				"Novo Nome",
				31,
				FuncaoEnum.ADMINISTRATIVO,
				"110.089.390-30"
		);

		Funcionario funcionarioExistente = createFuncionario(
				1,
				"123",
				"senha",
				"senha",
				"email@exemplo.com",
				"Nome",
				30,
				FuncaoEnum.REPARADOR,
				"123.456.789-09"
		);

		when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionarioExistente));
		when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Funcionario result = funcionarioService.update(funcionarioNovo, 1);

		assertEquals(funcionarioNovo.getSenha(), result.getSenha());
		assertEquals(funcionarioNovo.getConfirmacaoSenha(), result.getConfirmacaoSenha());
		assertEquals(funcionarioNovo.getEmail(), result.getEmail());
		assertEquals(funcionarioNovo.getNome(), result.getNome());
		assertEquals(funcionarioNovo.getIdade(), result.getIdade());
		assertEquals(funcionarioNovo.getFuncao(), result.getFuncao());
		assertEquals(funcionarioNovo.getCpf(), result.getCpf());

		verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
	}

	@Test
	void testUpdateFuncionarioNotFound() {
		Funcionario funcionarioNovo = createFuncionario(
				1,"1","novaSenha",
				"novaConfirmacaoSenha",
				"novoEmail@exemplo.com",
				"Novo Nome",
				31,
				FuncaoEnum.ADMINISTRATIVO,
				"123.456.789-10"
		);

		when(funcionarioRepository.findById(1)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> funcionarioService.update(funcionarioNovo, 1));
	}

	@Test
	void testUpdateFuncionarioInvalidData() {
		Funcionario funcionarioNovo = createFuncionario(
				1,
				null,
				null,
				null,
				null,
				null, null,null,
				null
		);

		Funcionario funcionarioExistente = createFuncionario(
				1,
				"123",
				"senha",
				"senha",
				"email@exemplo.com",
				"Nome",
				30,
				FuncaoEnum.REPARADOR,
				"123.456.789-09"
		);

		when(funcionarioRepository.findById(1)).thenReturn(Optional.of(funcionarioExistente));

		assertThrows(ValidacaoException.class, () -> funcionarioService.update(funcionarioNovo, 1));
	}

	@Test
	void testDeleteFuncionario() {
		when(funcionarioRepository.delete(1)).thenReturn(new Object());

		Object result = funcionarioService.delete(1);

		assertNotNull(result);
		verify(funcionarioRepository, times(1)).delete(1);
	}

	@Test
	void testDeleteFuncionarioIdInvalido() {
		assertThrows(UnprocessableEntityException.class, () -> funcionarioService.delete(-1));
	}

	@Test
	void testDeleteFuncionarioNotFound() {
		when(funcionarioRepository.delete(1)).thenReturn(null);

		assertThrows(NotFoundException.class, () -> funcionarioService.delete(1));
	}

}
