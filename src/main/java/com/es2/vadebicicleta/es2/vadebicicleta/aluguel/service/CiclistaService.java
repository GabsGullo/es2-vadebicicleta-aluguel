package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CiclistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CiclistaService {

    @Autowired
    private CiclistaRepository repository;

    public Ciclista getById(Integer id){return repository.findById(id);}

    public Ciclista save(Ciclista ciclista){return repository.save(ciclista);}
}
