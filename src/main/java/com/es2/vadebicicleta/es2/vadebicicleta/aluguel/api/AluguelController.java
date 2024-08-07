package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.AluguelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/aluguel")
public class AluguelController {

    private final AluguelService service;

    @Autowired
    public AluguelController(AluguelService service){
        this.service = service;
    }

    @PostMapping("/aluguel")
    public ResponseEntity<Aluguel> realizarAluguel(int ciclista, int tranca){
        Aluguel aluguelRealizado = service.realizarAluguel(ciclista, tranca);
        return ResponseEntity.ok().body(aluguelRealizado);
    }
}
