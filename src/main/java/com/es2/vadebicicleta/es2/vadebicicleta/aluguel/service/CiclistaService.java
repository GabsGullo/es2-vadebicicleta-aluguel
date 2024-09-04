package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.validator.Validator;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.*;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.integracao.ExternoClient;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CiclistaRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CiclistaService {

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

        cartaoDeCredito.setIdCiclista(ciclista.getId());
        cartaoDeCreditoService.register(cartaoDeCredito);

        if(ciclista.getStatus() == null)
            ciclista.setStatus(StatusEnum.AGUARDANDO_CONFIRMACAO);

        Ciclista ciclistaCadastrado = repository.save(ciclista);

        enviarEmail(ciclista);

        return ciclistaCadastrado;
    }

    private void enviarEmail(Ciclista ciclista) {

        EnderecoEmail email = new EnderecoEmail();
        email.setAssunto("Cadastro");
        email.setMensagem("Clique no link para confirmar seu cadastro\n\n54.144.22.151:8080/ciclista/"+ciclista.getId()+"/ativar");
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

    @PostConstruct
    public void initialData(){
        String nome = "Fulano Beltrano";
        String nascimentoString = "2021-05-02";
        LocalDate nascimento = LocalDate.parse(nascimentoString, DateTimeFormatter.ISO_DATE);
        NacionalidadeEnum nacionalidade = NacionalidadeEnum.BRASILEIRO;
        String senha = "ABC123";
        String urlFoto = "asdasdasd.jpg";
        String validadeString = "2024-12-01";
        LocalDate validade = LocalDate.parse(validadeString, DateTimeFormatter.ISO_DATE);
        CartaoDeCredito cartaoDeCredito = new CartaoDeCredito(1, nome, "4012001037141112",1, validade, "132");

        register(new Ciclista(1, StatusEnum.ATIVO, nome, nascimento, "78804034009", null ,nacionalidade,
                "user@example.com",urlFoto, senha), cartaoDeCredito);
        register(new Ciclista(2, StatusEnum.AGUARDANDO_CONFIRMACAO, nome, nascimento, "43943488039", null ,nacionalidade,
                "user2@example.com",urlFoto, senha), cartaoDeCredito);
        register(new Ciclista(3, StatusEnum.ATIVO, nome, nascimento, "10243164084", null ,nacionalidade,
                "user3@example.com",urlFoto, senha), cartaoDeCredito);
        register(new Ciclista(4, StatusEnum.ATIVO, nome, nascimento, "30880150017", null ,nacionalidade,
                "user4@example.com",urlFoto, senha), cartaoDeCredito);
    }

    public void clear(){
        repository.clear();
    }
}
