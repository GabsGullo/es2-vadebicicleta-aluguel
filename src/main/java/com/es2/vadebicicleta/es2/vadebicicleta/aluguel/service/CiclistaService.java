package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.Validator.Validator;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.*;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao.ExternoClient;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CiclistaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CiclistaService {

    public static final String OBJECT_NAME = "ciclista";
    public static final String PASSAPORTE = "passaporte";
    private final ExternoClient externoClient;
    private final CiclistaRepository repository;
    private final CartaoDeCreditoService cartaoDeCreditoService;
    private final Validator validator;

    @Autowired
    public CiclistaService(CiclistaRepository repository, CartaoDeCreditoService cartaoDeCreditoService, ExternoClient externoClient, Validator validator) {
        this.repository = repository;
        this.cartaoDeCreditoService = cartaoDeCreditoService;
        this.externoClient = externoClient;
        this.validator = validator;
    }

    public Ciclista getById(Integer idCiclista){
        return repository.findById(idCiclista).orElseThrow(
            () -> new NotFoundException("Ciclista não encontrado", HttpStatus.NOT_FOUND.toString()));
    }

    public Ciclista register(@Valid Ciclista ciclista, @Valid CartaoDeCredito cartaoDeCredito){
        validator.validateCiclista(ciclista);

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

    public Ciclista update(Ciclista ciclistaNovo, Integer idCiclista){

        Ciclista ciclistaCadastrado = getById(idCiclista);

        alterarDados(ciclistaNovo, ciclistaCadastrado);

        validator.validateCiclista(ciclistaCadastrado);

        return repository.save(ciclistaCadastrado);
    }

    private static void alterarDados(Ciclista ciclistaNovo, Ciclista ciclistaCadastrado) {
        ciclistaCadastrado.setCpf(ciclistaNovo.getCpf());
        ciclistaCadastrado.setEmail(ciclistaNovo.getEmail());
        ciclistaCadastrado.setNome(ciclistaNovo.getNome());
        ciclistaCadastrado.setSenha(ciclistaNovo.getSenha());
        ciclistaCadastrado.setNacionalidade(ciclistaNovo.getNacionalidade());
        ciclistaCadastrado.setNascimento(ciclistaNovo.getNascimento());
        ciclistaCadastrado.setUrlFotoDocumento(ciclistaNovo.getUrlFotoDocumento());
        ciclistaCadastrado.setPassaporte(ciclistaNovo.getPassaporte());
    }

    public Ciclista activate(Integer idCiclista){
        Ciclista ciclistaDesativado = getById(idCiclista);

        ciclistaDesativado.setStatus(StatusEnum.ATIVO);
        return repository.save(ciclistaDesativado);
    }

    public Ciclista alterarStatusAluguel(Integer idCiclista){
        Ciclista ciclista = getById(idCiclista);
        Boolean aluguelAtivo = ciclista.getAluguelAtivo();

        if(aluguelAtivo)
            ciclista.setAluguelAtivo(Boolean.FALSE);
        else
            ciclista.setAluguelAtivo(Boolean.TRUE);

        return repository.save(ciclista);
    }

}
