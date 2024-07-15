package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CartaoDeCreditoDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.NotFoundException;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.CartaoDeCreditoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CartaoDeCreditoService {

    private final CartaoDeCreditoRepository repository;

    @Autowired
    public CartaoDeCreditoService(CartaoDeCreditoRepository repository){
        this.repository = repository;
    }

    public void register(@Valid CartaoDeCredito cartaoDeCredito){
        repository.save(cartaoDeCredito);
    }

    public CartaoDeCredito getCartaoByCiclistaId(Integer idCiclista){
        return repository.findById(idCiclista).orElseThrow(
                () -> new NotFoundException("Cartão não encontrado", HttpStatus.NOT_FOUND.toString()));
    }

    public void update(CartaoDeCreditoDTO cartaoNovo, Integer idCiclista){
        CartaoDeCredito cartaoDeCreditoCadastrado = getCartaoByCiclistaId(idCiclista);

        cartaoDeCreditoCadastrado.setNomeTitular(cartaoNovo.getNomeTitular());
        cartaoDeCreditoCadastrado.setNumero(cartaoNovo.getNumero());
        cartaoDeCreditoCadastrado.setValidade(cartaoNovo.getValidade());
        cartaoDeCreditoCadastrado.setCvv(cartaoNovo.getCvv());

        repository.save(cartaoDeCreditoCadastrado);
    }
}