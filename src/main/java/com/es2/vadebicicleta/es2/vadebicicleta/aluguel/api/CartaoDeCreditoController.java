package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CartaoDeCreditoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CartaoDeCreditoController {

    private final CartaoDeCreditoService service;

    public CartaoDeCreditoController(CartaoDeCreditoService service) {
        this.service = service;
    }

    @GetMapping("cartaoDeCredito/{idCiclista}")
    public ResponseEntity<CartaoDeCredito>getCartao(@PathVariable Integer idCiclista){
        CartaoDeCredito cartao = service.getCartaoByCiclistaId(idCiclista);
        return ResponseEntity.ok().body(cartao);
    }

}
