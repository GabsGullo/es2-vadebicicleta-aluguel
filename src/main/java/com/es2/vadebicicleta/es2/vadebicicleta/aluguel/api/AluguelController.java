package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/aluguel")
public class AluguelController {

    @GetMapping
    public ResponseEntity healthcheck(){
        return new ResponseEntity<>("teste", HttpStatus.OK);
    }
}
