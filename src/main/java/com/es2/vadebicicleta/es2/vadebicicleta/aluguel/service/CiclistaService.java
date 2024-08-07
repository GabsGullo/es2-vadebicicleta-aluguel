package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.*;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao.ExternoClient;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CiclistaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
public class CiclistaService {

    public static final String OBJECT_NAME = "ciclista";
    public static final String PASSAPORTE = "passaporte";
    private final ExternoClient externoClient;
    private final CiclistaRepository repository;
    private final CartaoDeCreditoService cartaoDeCreditoService;

    @Autowired
    public CiclistaService(CiclistaRepository repository, CartaoDeCreditoService cartaoDeCreditoService, ExternoClient externoClient) {
        this.repository = repository;
        this.cartaoDeCreditoService = cartaoDeCreditoService;
        this.externoClient = externoClient;
    }

    public Ciclista getById(Integer idCiclista){
        return repository.findById(idCiclista).orElseThrow(
            () -> new NotFoundException("Ciclista não encontrado", HttpStatus.NOT_FOUND.toString()));
    }

    public Ciclista register(@Valid Ciclista ciclista, @Valid CartaoDeCredito cartaoDeCredito){
        validateCiclista(ciclista);

        ciclista.setStatus(StatusEnum.AGUARDANDO_CONFIRMACAO);
        ciclista.setAluguelAtivo(false);
        cartaoDeCreditoService.register(cartaoDeCredito);

        Ciclista ciclistaCadastrado = repository.save(ciclista);

        enviarEmail(ciclista);

        return ciclistaCadastrado;
    }

    private void enviarEmail(Ciclista ciclista) {

        EnderecoEmail email = new EnderecoEmail();
        email.setAssunto("Cadastro");
        email.setMensagem("Cadastro concluido com sucesso");
        email.setEmail(ciclista.getEmail());
        externoClient.enviarEmail(email);

    }

    public Ciclista update(CiclistaInPutDTO ciclistaNovo, Integer idCiclista){

        Ciclista ciclistaCadastrado = getById(idCiclista);

        ciclistaCadastrado.setCpf(ciclistaNovo.getCpf());
        ciclistaCadastrado.setEmail(ciclistaNovo.getEmail());
        ciclistaCadastrado.setNome(ciclistaNovo.getNome());
        ciclistaCadastrado.setNacionalidade(NacionalidadeEnum.valueOf(ciclistaNovo.getNacionalidade()));
        ciclistaCadastrado.setNascimento(ciclistaNovo.getNascimento());
        ciclistaCadastrado.setUrlFotoDocumento(ciclistaNovo.getUrlFotoDocumento());
        ciclistaCadastrado.setPassaporte(ciclistaNovo.getPassaporte());

        validateCiclista(ciclistaCadastrado);

        return repository.save(ciclistaCadastrado);
    }

    public Ciclista activate(Integer idCiclista){
        Ciclista ciclistaDesativado = getById(idCiclista);

        ciclistaDesativado.setStatus(StatusEnum.ATIVO);
        return repository.save(ciclistaDesativado);
    }

    public Ciclista alterarStatusAluguel(Integer idCiclista){
        Ciclista ciclista = getById(idCiclista);

        ciclista.setAluguelAtivo(true);
        return repository.save(ciclista);
    }

    private void validateCiclista(Ciclista ciclista) {
        BindingResult result = new BeanPropertyBindingResult(ciclista, OBJECT_NAME);

        validateNome(ciclista, result);
        validateNascimento(ciclista, result);
        validateNacionalidade(ciclista, result);
        validateEmail(ciclista, result);
        validateSenha(ciclista, result);
        validateDocumentos(ciclista, result);

        if (result.hasErrors()) {
            throw new ValidacaoException(result);
        }
    }

    private void validateNome(Ciclista ciclista, BindingResult result) {
        if (ciclista.getNome() == null || ciclista.getNome().isEmpty()) {
            result.addError(new FieldError(OBJECT_NAME, "nome", "Nome não pode ser nulo ou vazio"));
        }
    }

    private void validateNascimento(Ciclista ciclista, BindingResult result) {
        if (ciclista.getNascimento() == null || ciclista.getNascimento().isEmpty()) {
            result.addError(new FieldError(OBJECT_NAME, "nascimento", "Nascimento não pode ser nulo ou vazio"));
        }
    }

    private void validateNacionalidade(Ciclista ciclista, BindingResult result) {
        if (ciclista.getNacionalidade() == null) {
            result.addError(new FieldError(OBJECT_NAME, "nacionalidade", "Nacionalidade não pode ser nula"));
        }
    }

    private void validateEmail(Ciclista ciclista, BindingResult result) {
        if (ciclista.getEmail() == null || !isValidEmail(ciclista.getEmail())) {
            result.addError(new FieldError(OBJECT_NAME, "email", "Email inválido"));
        }
    }

    private void validateSenha(Ciclista ciclista, BindingResult result) {
        if (ciclista.getSenha() == null || ciclista.getSenha().isEmpty()) {
            result.addError(new FieldError(OBJECT_NAME, "senha", "Senha não pode ser nula ou vazia"));
        }
    }

    private void validateDocumentos(Ciclista ciclista, BindingResult result) {

        if (ciclista.getCpf() == null && ciclista.getPassaporte() == null) {
            result.addError(new FieldError(OBJECT_NAME, "cpf", "Nenhum documento informado"));
            result.addError(new FieldError(OBJECT_NAME, PASSAPORTE, "Nenhum documento informado"));
        } else if (ciclista.getCpf() != null && ciclista.getPassaporte() != null) {
            result.addError(new FieldError(OBJECT_NAME, "cpf", "CPF e passaporte informados"));
            result.addError(new FieldError(OBJECT_NAME, PASSAPORTE, "CPF e passaporte informados"));
        } else if (ciclista.getCpf() == null && ciclista.getNacionalidade() == NacionalidadeEnum.BRASILEIRO){
            result.addError(new FieldError(OBJECT_NAME, "cpf", "CPF precisa ser informado"));
        } else if (ciclista.getCpf() != null && ciclista.getNacionalidade() != NacionalidadeEnum.BRASILEIRO){
            result.addError(new FieldError(OBJECT_NAME, PASSAPORTE, "Passaporte precisa ser informado"));
            result.addError(new FieldError(OBJECT_NAME, "cpf", "CPF não deve ser informado"));
        } else {
            validateCpf(ciclista, result);
            validatePassaporte(ciclista, result);
        }
    }

    private void validateCpf(Ciclista ciclista, BindingResult result) {
        if (ciclista.getCpf() != null && !isValidCPF(ciclista.getCpf())) {
            result.addError(new FieldError(OBJECT_NAME, "cpf", "CPF inválido"));
        }
    }

    private void validatePassaporte(Ciclista ciclista, BindingResult result) {
        if (ciclista.getPassaporte() != null) {
            if (ciclista.getPassaporte().getNumero() == null || ciclista.getPassaporte().getNumero().isEmpty()) {
                result.addError(new FieldError(OBJECT_NAME, "passaporte.numero", "Número do passaporte não pode ser nulo ou vazio"));
            }
            if (ciclista.getPassaporte().getValidade() == null || ciclista.getPassaporte().getValidade().isEmpty()) {
                result.addError(new FieldError(OBJECT_NAME, "passaporte.validade", "Validade do passaporte não pode ser nula ou vazia"));
            }
            if (ciclista.getPassaporte().getPais() == null || ciclista.getPassaporte().getPais().isEmpty()) {
                result.addError(new FieldError(OBJECT_NAME, "passaporte.pais", "País do passaporte não pode ser nulo ou vazio"));
            }
        }
    }

    private boolean isValidCPF(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^\\d]", "");

        // Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        try {
            // Cálculo do primeiro dígito verificador
            int sum1 = 0;
            for (int i = 0; i < 9; i++) {
                sum1 += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int check1 = 11 - (sum1 % 11);
            if (check1 >= 10) {
                check1 = 0;
            }

            // Cálculo do segundo dígito verificador
            int sum2 = 0;
            for (int i = 0; i < 10; i++) {
                sum2 += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int check2 = 11 - (sum2 % 11);
            if (check2 >= 10) {
                check2 = 0;
            }

            // Verifica se os dígitos verificadores calculados são iguais aos informados
            return (check1 == Character.getNumericValue(cpf.charAt(9)) &&
                    check2 == Character.getNumericValue(cpf.charAt(10)));

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        // Implementação fictícia, substitua pela sua lógica de validação de Email
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
