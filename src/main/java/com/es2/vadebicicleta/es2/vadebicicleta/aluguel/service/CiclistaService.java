package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.Exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.Exception.UnprocessableEntityException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.CiclistaConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CiclistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CiclistaService {

    private final CiclistaRepository repository;

    @Autowired
    public CiclistaService(CiclistaRepository repository) {
        this.repository = repository;
    }

    public Ciclista getById(Integer idCiclista){
        if(idCiclista < 0){
            throw new UnprocessableEntityException("Chave Invalida", HttpStatus.UNPROCESSABLE_ENTITY.toString());
        }

        return repository.findById(idCiclista).orElseThrow(
            () -> new NotFoundException("Ciclista não encontrado", HttpStatus.NOT_FOUND.toString()));
    }

    public Ciclista save(Ciclista ciclista){return repository.save(ciclista);}

    public Ciclista update(CiclistaInDTO ciclistaNovo, Integer idCiclista){
        Ciclista ciclistaCadastrado = getById(idCiclista);
        if(ciclistaCadastrado != null){

            Ciclista ciclistaAtualizado = new Ciclista();
            ciclistaAtualizado.setMeioDePagamento(ciclistaCadastrado.getMeioDePagamento());
            ciclistaAtualizado.setSenha(ciclistaCadastrado.getSenha());

            ciclistaAtualizado.setCpf(ciclistaNovo.getCpf());
            ciclistaAtualizado.setEmail(ciclistaNovo.getEmail());
            ciclistaAtualizado.setNome(ciclistaNovo.getNome());
            ciclistaAtualizado.setNacionalidade(ciclistaNovo.getNacionalidade());
            ciclistaAtualizado.setNascimento(ciclistaNovo.getNascimento());
            ciclistaAtualizado.setUrlFotoDocumento(ciclistaNovo.getUrlFotoDocumento());
            ciclistaAtualizado.setPassaporte(ciclistaNovo.getPassaporte());

            return repository.save(ciclistaAtualizado);
        }

        return null;
    }
}
