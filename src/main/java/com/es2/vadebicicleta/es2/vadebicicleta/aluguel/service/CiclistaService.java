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

    private void validateCiclista(Ciclista ciclista){
        String objectName = "ciclista";
        BindingResult result = new BeanPropertyBindingResult(ciclista, objectName);

        if (ciclista.getStatus() == null) {
            result.addError(new FieldError(objectName, "status", "Status não pode ser nulo"));
        }
        if (ciclista.getNome() == null || ciclista.getNome().isEmpty()) {
            result.addError(new FieldError(objectName, "nome", "Nome não pode ser nulo ou vazio"));
        }
        if (ciclista.getNascimento() == null || ciclista.getNascimento().isEmpty()) {
            result.addError(new FieldError(objectName, "nascimento", "Nascimento não pode ser nulo ou vazio"));
        }
        if (ciclista.getNacionalidade() == null) {
            result.addError(new FieldError(objectName, "nacionalidade", "Nacionalidade não pode ser nula"));
        }
        if (ciclista.getEmail() == null || !isValidEmail(ciclista.getEmail())) {
            result.addError(new FieldError(objectName, "email", "Email inválido"));
        }
        if (ciclista.getSenha() == null || ciclista.getSenha().isEmpty()) {
            result.addError(new FieldError(objectName, "senha", "Senha não pode ser nula ou vazia"));
        }

        boolean temUmDocumentoValido = true;
        if(ciclista.getCpf() == null && ciclista.getPassaporte() == null){
            result.addError(new FieldError(objectName, "cpf", "Nenhum documento informado"));
            result.addError(new FieldError(objectName, "passaporte", "Nenhum documento informado"));
            temUmDocumentoValido = false;
        }
        if(ciclista.getCpf() != null && ciclista.getPassaporte() != null){
            result.addError(new FieldError(objectName, "cpf", "CPF e passaporte informados"));
            result.addError(new FieldError(objectName, "passaporte", "CPF e passaporte informados"));
        }
        if (ciclista.getCpf() != null && !isValidCPF(ciclista.getCpf())) {
            result.addError(new FieldError(objectName, "cpf", "CPF inválido"));
        }
        if (ciclista.getPassaporte() != null) {
            if (ciclista.getPassaporte().getNumero() == null || ciclista.getPassaporte().getNumero().isEmpty()) {
                result.addError(new FieldError(objectName, "passaporte.numero", "Número do passaporte não pode ser nulo ou vazio"));
            }
            if (ciclista.getPassaporte().getValidade() == null || ciclista.getPassaporte().getValidade().isEmpty()) {
                result.addError(new FieldError(objectName, "passaporte.validade", "Validade do passaporte não pode ser nula ou vazia"));
            }
            if (ciclista.getPassaporte().getPais() == null || ciclista.getPassaporte().getPais().isEmpty()) {
                result.addError(new FieldError(objectName, "passaporte.pais", "País do passaporte não pode ser nulo ou vazio"));
            }
        }

        if (result.hasErrors()) {
            throw new ValidacaoException(result);
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
