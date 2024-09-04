package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CartaoDeCreditoRepository;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.validator.Validator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CartaoDeCreditoService {

    private final CartaoDeCreditoRepository repository;
    private final Validator validator;

    @Autowired
    public CartaoDeCreditoService(CartaoDeCreditoRepository repository, Validator validator){
        this.repository = repository;
        this.validator = validator;
    }

    public void register(@Valid CartaoDeCredito cartaoDeCredito){
        validator.validateCartaoDeCredito(cartaoDeCredito);
        repository.save(cartaoDeCredito);
    }

    public CartaoDeCredito getCartaoByCiclistaId(Integer idCiclista){
        return repository.findById(idCiclista).orElseThrow(
                () -> new NotFoundException("Cartão não encontrado", HttpStatus.NOT_FOUND.toString()));
    }

    public void update(CartaoDeCredito cartaoNovo, Integer idCiclista){
        CartaoDeCredito cartaoDeCreditoCadastrado = getCartaoByCiclistaId(idCiclista);

        cartaoDeCreditoCadastrado.setNomeTitular(cartaoNovo.getNomeTitular());
        cartaoDeCreditoCadastrado.setNumero(cartaoNovo.getNumero());
        cartaoDeCreditoCadastrado.setValidade(cartaoNovo.getValidade());
        cartaoDeCreditoCadastrado.setCvv(cartaoNovo.getCvv());

        validator.validateCartaoDeCredito(cartaoDeCreditoCadastrado);
        repository.save(cartaoDeCreditoCadastrado);
    }

    public void clear(){
        repository.clear();
    }
}