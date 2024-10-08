package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioInDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioOutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.FuncionarioService;
import jakarta.validation.Valid;
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
    public ResponseEntity<FuncionarioOutDTO> postFuncionario(@Valid @RequestBody FuncionarioInDTO dto){
        Funcionario funcionario = converter.inDtoToEntity(dto);
        Funcionario funcionarioCadastrado = service.save(funcionario);
        FuncionarioOutDTO outDTO = converter.entityToOutDTO(funcionarioCadastrado);

        return ResponseEntity.ok().body(outDTO);
    }

    @PutMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<FuncionarioOutDTO> putFuncionario(@Valid @RequestBody FuncionarioInDTO funcionarioNovoDTO, @PathVariable Integer idFuncionario){
        Funcionario funcionarioNovo = converter.inDtoToEntity(funcionarioNovoDTO);
        Funcionario funcionarioCadastrado = service.update(funcionarioNovo, idFuncionario);
        FuncionarioOutDTO funcionarioAtualizado = converter.entityToOutDTO(funcionarioCadastrado);

        return ResponseEntity.ok().body(funcionarioAtualizado);
    }

    @DeleteMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<Object> deleteFuncionario(@PathVariable Integer idFuncionario){
        service.delete(idFuncionario);
        return ResponseEntity.ok().build();
    }

}
