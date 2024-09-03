package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Aluguel;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelDevolucaoDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelOutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.AluguelRegistroDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.DevolucaoRegistroDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.AluguelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AluguelController {

    private final AluguelService service;
    private final AluguelDevolucaoConverter converter;

    @Autowired
    public AluguelController(AluguelService service, AluguelDevolucaoConverter converter){
        this.service = service;
        this.converter = converter;
    }

    @PostMapping("/aluguel")
    public ResponseEntity<AluguelOutDTO> realizarAluguel(@RequestBody AluguelRegistroDTO aluguelRegistroDTO){
        Aluguel aluguelRealizado = service.realizarAluguel(aluguelRegistroDTO.getCiclista(), aluguelRegistroDTO.getTrancaInicio());
        AluguelOutDTO aluguelOutDTO = converter.aluguelToOutDTO(aluguelRealizado);
        return ResponseEntity.ok().body(aluguelOutDTO);
    }

    @PostMapping("/devolucao")
    public ResponseEntity<AluguelDevolucaoDTO> realizarDevolucao(@RequestBody DevolucaoRegistroDTO devolucaoRegistroDTO){
        Aluguel aluguelFinalizado = service.realizarDevolucao(devolucaoRegistroDTO.getIdTranca(), devolucaoRegistroDTO.getIdBicicleta());
        AluguelDevolucaoDTO devolucaoDTO = converter.aluguelToDevolucao(aluguelFinalizado);
        return ResponseEntity.ok().body(devolucaoDTO);
    }

}
