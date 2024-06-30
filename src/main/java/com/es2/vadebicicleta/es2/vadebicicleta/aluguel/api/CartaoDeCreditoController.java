package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CartaoDeCreditoDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CartaoDeCreditoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CartaoDeCreditoController {

    private final CartaoDeCreditoService service;

    public CartaoDeCreditoController(CartaoDeCreditoService service) {
        this.service = service;
    }

    @GetMapping("cartaoDeCredito/{idCiclista}")
    public ResponseEntity<CartaoDeCredito>getCartao(@PathVariable Integer idCiclista){
        CartaoDeCredito cartaoDeCredito = service.getCartaoByCiclistaId(idCiclista);
        return ResponseEntity.ok().body(cartaoDeCredito);
    }

    @PutMapping("cartaoDeCredito/{idCiclista}")
    public ResponseEntity<CartaoDeCreditoDTO> putCartaoDeCredito(@Valid @RequestBody CartaoDeCreditoDTO cartaoNovo, @PathVariable Integer idCiclista){
        service.update(cartaoNovo, idCiclista);
        return ResponseEntity.ok().build();
    }
}
