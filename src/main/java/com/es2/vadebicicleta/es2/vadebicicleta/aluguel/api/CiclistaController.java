package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.CartaoDeCredito;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Ciclista;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.*;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.CiclistaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CiclistaController {

    private final CiclistaService service;

    private final CiclistaConverter ciclistaConverter;

    private final CartaoDeCreditoConverter cartaoDeCreditoConverter;

    @Autowired
    public CiclistaController(CiclistaService service, CiclistaConverter ciclistaConverter, CartaoDeCreditoConverter cartaoDeCreditoConverter) {
        this.service = service;
        this.ciclistaConverter = ciclistaConverter;
        this.cartaoDeCreditoConverter = cartaoDeCreditoConverter;
    }

    @GetMapping("/ciclista/{idCiclista}")
    public ResponseEntity <CiclistaOutDTO> getCiclista(@PathVariable Integer idCiclista){
        Ciclista ciclista = service.getById(idCiclista);
        return ResponseEntity.ok().body(ciclistaConverter.entityToOutDTO(ciclista));
    }

    //CADASTRAR CLIENTE UC01
    @PostMapping("/ciclista")
    public ResponseEntity<CiclistaOutDTO> postCiclista(@Valid @RequestBody RegistoCartaoCiclistaDTO registo){
        Ciclista ciclista = ciclistaConverter.inPostDtoToEntity(registo.getCiclista());
        CartaoDeCredito cartaoDeCredito = cartaoDeCreditoConverter.inDtoToEntity(registo.getCartaoDeCredito());

        Ciclista ciclistaCadastrado = service.register(ciclista, cartaoDeCredito);

        CiclistaOutDTO ciclistaDTO = ciclistaConverter.entityToOutDTO(ciclistaCadastrado);
        return ResponseEntity.ok().body(ciclistaDTO);
    }

    //ALTERAR DADOS DO CLIENTE UC06
    @PutMapping("/ciclista/{idCiclista}")
    public ResponseEntity<CiclistaOutDTO> putCiclista(@Valid @RequestBody CiclistaInPutDTO ciclistaNovo, @PathVariable Integer idCiclista){
        Ciclista ciclistaCadastrado = service.update(ciclistaNovo, idCiclista);
        CiclistaOutDTO ciclistaAtualizado = ciclistaConverter.entityToOutDTO(ciclistaCadastrado);
        return ResponseEntity.ok().body(ciclistaAtualizado);
    }

    @GetMapping("/ciclista/{idCiclista}/ativar")
    public ResponseEntity<CiclistaOutDTO> ativarCiclista(@PathVariable Integer idCiclista){
        Ciclista ciclistaAtivado = service.activate(idCiclista);
        CiclistaOutDTO ciclistaAtualizado = ciclistaConverter.entityToOutDTO(ciclistaAtivado);
        return ResponseEntity.ok().body(ciclistaAtualizado);
    }


}
