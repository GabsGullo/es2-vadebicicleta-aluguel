package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.repository.IdGenerator;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.AluguelService;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CartaoDeCreditoService;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CiclistaService;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClearController {

    private final CiclistaService ciclistaService;
    private final FuncionarioService funcionarioService;
    private final AluguelService aluguelService;
    private final CartaoDeCreditoService cartaoDeCreditoService;
    private final IdGenerator idGenerator;

    public ClearController(CiclistaService ciclistaService, FuncionarioService funcionarioService, AluguelService aluguelService, CartaoDeCreditoService cartaoDeCreditoService, IdGenerator idGenerator) {
        this.ciclistaService = ciclistaService;
        this.funcionarioService = funcionarioService;
        this.aluguelService = aluguelService;
        this.cartaoDeCreditoService = cartaoDeCreditoService;
        this.idGenerator = idGenerator;
    }

    @GetMapping("/restaurarDados")
    public ResponseEntity<Void> restaurarDados(){
        funcionarioService.clear();
        aluguelService.clear();
        cartaoDeCreditoService.clear();
        ciclistaService.clear();
        idGenerator.clear();
        ciclistaService.initialData();
        return ResponseEntity.ok().build();
    }
}
