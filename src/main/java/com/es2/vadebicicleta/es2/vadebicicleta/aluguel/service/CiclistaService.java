package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.NacionalidadeEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.StatusEnum;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CiclistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    public Ciclista register(Ciclista ciclista, CartaoDeCredito cartaoDeCredito){
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
}
