package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelDevolucaoDTO;
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
    private final AluguelDevolucaoConverter converter;

    @Autowired
    public AluguelController(AluguelService service, AluguelDevolucaoConverter converter){
        this.service = service;
        this.converter = converter;
    }

    @PostMapping("/aluguel")
    public ResponseEntity<Aluguel> realizarAluguel(Integer ciclista, Integer tranca){
        Aluguel aluguelRealizado = service.realizarAluguel(ciclista, tranca);
        return ResponseEntity.ok().body(aluguelRealizado);
    }

    @PostMapping("/devolucao")
    public ResponseEntity<AluguelDevolucaoDTO> realizarDevolucao(Integer idTranca, Integer idBicicleta){
        Aluguel aluguelFinalizado = service.realizarDevolucao(idTranca, idBicicleta);
        AluguelDevolucaoDTO devolucaoDTO = converter.aluguelToDevolucao(aluguelFinalizado);
        return ResponseEntity.ok().body(devolucaoDTO);
    }

}
