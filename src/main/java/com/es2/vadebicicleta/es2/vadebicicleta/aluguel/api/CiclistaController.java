package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaOutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CiclistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class CiclistaController {

    @Autowired
    private CiclistaService service;

    @Autowired
    private CiclistaConverter converter;

    @GetMapping("/ciclista/{idCiclista}")
    public ResponseEntity <CiclistaOutDTO> getCiclista(@PathVariable Integer idCiclista){
        Optional<Ciclista> ciclista = service.getById(idCiclista);
        return ciclista.map(value -> ResponseEntity.ok(converter.entityToDTO(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //CADASTRAR CLIENTE UC01
    @PostMapping("/ciclista")
    public ResponseEntity<CiclistaOutDTO> postCiclista(@RequestBody Ciclista ciclista){
        Ciclista ciclistaCadastrado = service.save(ciclista);
        if(ciclistaCadastrado == null)
            return ResponseEntity.notFound().build();

        CiclistaOutDTO ciclistaDTO = converter.entityToDTO(ciclista);
        return ResponseEntity.ok().body(ciclistaDTO);
    }

    //ALTERAR DADOS DO CLIENTE UC06
    @PutMapping("/ciclista/{idCiclista}")
    public ResponseEntity<CiclistaOutDTO> putCiclista(@RequestBody CiclistaInDTO ciclistaNovo, @PathVariable Integer idCiclista){
        Ciclista ciclistaCadastrado = service.update(ciclistaNovo, idCiclista);
        if(ciclistaCadastrado == null)
            return ResponseEntity.notFound().build();

        CiclistaOutDTO ciclistaAtualizado = converter.entityToDTO(ciclistaCadastrado);
        return ResponseEntity.ok().body(ciclistaAtualizado);
    }
}
