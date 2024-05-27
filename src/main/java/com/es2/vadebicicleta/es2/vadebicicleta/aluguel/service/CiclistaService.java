package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.CiclistaConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CiclistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CiclistaService {

    private final CiclistaRepository repository;

    @Autowired
    public CiclistaService(CiclistaRepository repository) {
        this.repository = repository;
    }

    public Optional<Ciclista> getById(Integer idCiclista){return repository.findById(idCiclista);}

    public Ciclista save(Ciclista ciclista){return repository.save(ciclista);}

    public Ciclista update(CiclistaInDTO ciclistaNovo, Integer idCiclista){
        Optional<Ciclista> ciclistaCadastrado = getById(idCiclista);
        if(ciclistaCadastrado.isPresent()){

            Ciclista ciclistaAtualizado = ciclistaCadastrado.get();
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
