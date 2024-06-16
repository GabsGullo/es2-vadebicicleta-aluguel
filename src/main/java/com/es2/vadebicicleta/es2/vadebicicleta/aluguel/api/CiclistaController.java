package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPostDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaInPutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.CiclistaOutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CiclistaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CiclistaController {

    private final CiclistaService service;

    private final CiclistaConverter converter;
    @Autowired
    public CiclistaController(CiclistaService service, CiclistaConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping("/ciclista/{idCiclista}")
    public ResponseEntity <CiclistaOutDTO> getCiclista(@PathVariable Integer idCiclista){
        Ciclista ciclista = service.getById(idCiclista);
        return ResponseEntity.ok().body(converter.entityToOutDTO(ciclista));
    }

    //CADASTRAR CLIENTE UC01
    @PostMapping("/ciclista")
    public ResponseEntity<CiclistaOutDTO> postCiclista(@Valid @RequestBody CiclistaInPostDTO dto){
        Ciclista ciclista = converter.inPostDtoToEntity(dto);
        Ciclista ciclistaCadastrado = service.register(ciclista);
        CiclistaOutDTO ciclistaDTO = converter.entityToOutDTO(ciclistaCadastrado);
        return ResponseEntity.ok().body(ciclistaDTO);
    }

    //ALTERAR DADOS DO CLIENTE UC06
    @PutMapping("/ciclista/{idCiclista}")
    public ResponseEntity<CiclistaOutDTO> putCiclista(@Valid @RequestBody CiclistaInPutDTO ciclistaNovo, @PathVariable Integer idCiclista){
        Ciclista ciclistaCadastrado = service.update(ciclistaNovo, idCiclista);
        CiclistaOutDTO ciclistaAtualizado = converter.entityToOutDTO(ciclistaCadastrado);
        return ResponseEntity.ok().body(ciclistaAtualizado);
    }

    @GetMapping("/ciclista/{idCiclista}/ativar")
    public ResponseEntity<CiclistaOutDTO> ativarCiclista(@PathVariable Integer idCiclista){
        Ciclista ciclistaAtivado = service.activate(idCiclista);
        CiclistaOutDTO ciclistaAtualizado = converter.entityToOutDTO(ciclistaAtivado);
        return ResponseEntity.ok().body(ciclistaAtualizado);
    }
}
