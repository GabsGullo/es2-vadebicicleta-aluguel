package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioInDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioOutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class FuncionarioController {

    private final FuncionarioService service;

    private final FuncionarioConverter converter;

    @Autowired
    public FuncionarioController(FuncionarioService service, FuncionarioConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping("/funcionario")
    public ResponseEntity<List<Funcionario>> getFuncionarios(){
        return ResponseEntity.ok().body(service.getAllFuncionarios());
    }

    @GetMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<FuncionarioOutDTO> getFuncionario(@PathVariable Integer idFuncionario){
        Funcionario funcionario = service.getById(idFuncionario);
        return ResponseEntity.ok().body(converter.entityToOutDTO(funcionario));
    }

    @PostMapping("/funcionario")
    public ResponseEntity<FuncionarioOutDTO> postFuncionario(@RequestBody FuncionarioInDTO dto){
        Funcionario funcionario = converter.inDtoToEntity(dto);
        Funcionario funcionarioCadastrado = service.save(funcionario);

        if(funcionarioCadastrado == null)
            return ResponseEntity.unprocessableEntity().build();

        FuncionarioOutDTO outDTO = converter.entityToOutDTO(funcionarioCadastrado);
        return ResponseEntity.ok().body(outDTO);
    }

    @PutMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<FuncionarioOutDTO> putFuncionario(@RequestBody FuncionarioInDTO funcionarioNovo, @PathVariable Integer idFuncionario){
        Funcionario funcionarioCadastrado = service.update(funcionarioNovo, idFuncionario);
        if(funcionarioCadastrado == null)
            return ResponseEntity.notFound().build();

        FuncionarioOutDTO funcionarioAtualizado = converter.entityToOutDTO(funcionarioCadastrado);
        return ResponseEntity.ok().body(funcionarioAtualizado);
    }

    @DeleteMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<Object> deleteFuncionario(@PathVariable Integer idFuncionario){
        Object object = service.delete(idFuncionario);
        if(object == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().build();
    }

}
