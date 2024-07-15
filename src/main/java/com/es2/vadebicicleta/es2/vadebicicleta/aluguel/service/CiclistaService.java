package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.NacionalidadeEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.StatusEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.ValidacaoException;
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
    private final CiclistaRepository repository;
    private final CartaoDeCreditoService cartaoDeCreditoService;

    @Autowired
    public CiclistaService(CiclistaRepository repository, CartaoDeCreditoService cartaoDeCreditoService) {
        this.repository = repository;
        this.cartaoDeCreditoService = cartaoDeCreditoService;
    }

    public Ciclista getById(Integer idCiclista){
        return repository.findById(idCiclista).orElseThrow(
            () -> new NotFoundException("Ciclista não encontrado", HttpStatus.NOT_FOUND.toString()));
    }

    public Ciclista register(@Valid Ciclista ciclista, @Valid CartaoDeCredito cartaoDeCredito){
        validateCiclista(ciclista);

        ciclista.setStatus(StatusEnum.AGUARDANDO_CONFIRMACAO);
        cartaoDeCreditoService.register(cartaoDeCredito);

        return repository.save(ciclista);
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

        return repository.save(ciclistaCadastrado);
    }

    public Ciclista activate(Integer idCiclista){
        Ciclista ciclistaDesativado = getById(idCiclista);

        ciclistaDesativado.setStatus(StatusEnum.ATIVO);
        return repository.save(ciclistaDesativado);
    }

    private void validateCiclista(Ciclista ciclista) {
        BindingResult result = new BeanPropertyBindingResult(ciclista, OBJECT_NAME);

        validateStatus(ciclista, result);
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

    private void validateStatus(Ciclista ciclista, BindingResult result) {
        if (ciclista.getStatus() == null) {
            result.addError(new FieldError(OBJECT_NAME, "status", "Status não pode ser nulo"));
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
            result.addError(new FieldError(OBJECT_NAME, "passaporte", "Nenhum documento informado"));
        } else if (ciclista.getCpf() != null && ciclista.getPassaporte() != null) {
            result.addError(new FieldError(OBJECT_NAME, "cpf", "CPF e passaporte informados"));
            result.addError(new FieldError(OBJECT_NAME, "passaporte", "CPF e passaporte informados"));
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
        // Implementação fictícia, substitua pela sua lógica de validação de CPF
        return cpf != null && cpf.matches("\\d{11}");
    }

    private boolean isValidEmail(String email) {
        // Implementação fictícia, substitua pela sua lógica de validação de Email
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
