package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;

import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.FuncionarioController;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.api.FuncionarioConverter;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.Funcionario;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioInDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.domain.dto.FuncionarioOutDTO;
import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.service.FuncionarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuncionarioControllerTest {

    @Mock
    private FuncionarioService funcionarioService;

    @Mock
    private FuncionarioConverter funcionarioConverter;

    @InjectMocks
    private FuncionarioController funcionarioController;

    @Test
    void testGetFuncionarios() {
        Funcionario funcionario = new Funcionario();
        when(funcionarioService.getAllFuncionarios()).thenReturn(Collections.singletonList(funcionario));

        ResponseEntity<List<Funcionario>> response = funcionarioController.getFuncionarios();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(funcionarioService, times(1)).getAllFuncionarios();
    }

    @Test
    void testGetFuncionario() {
        Integer idFuncionario = 1;
        Funcionario funcionario = new Funcionario();
        FuncionarioOutDTO funcionarioOutDTO = new FuncionarioOutDTO();

        when(funcionarioService.getById(idFuncionario)).thenReturn(funcionario);
        when(funcionarioConverter.entityToOutDTO(funcionario)).thenReturn(funcionarioOutDTO);

        ResponseEntity<FuncionarioOutDTO> response = funcionarioController.getFuncionario(idFuncionario);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(funcionarioOutDTO, response.getBody());
        verify(funcionarioService, times(1)).getById(idFuncionario);
        verify(funcionarioConverter, times(1)).entityToOutDTO(funcionario);
    }

    @Test
    void testPostFuncionario() {
        FuncionarioInDTO dto = new FuncionarioInDTO();
        Funcionario funcionario = new Funcionario();
        Funcionario funcionarioCadastrado = new Funcionario();
        FuncionarioOutDTO funcionarioOutDTO = new FuncionarioOutDTO();

        when(funcionarioConverter.inDtoToEntity(dto)).thenReturn(funcionario);
        when(funcionarioService.save(funcionario)).thenReturn(funcionarioCadastrado);
        when(funcionarioConverter.entityToOutDTO(funcionarioCadastrado)).thenReturn(funcionarioOutDTO);

        ResponseEntity<FuncionarioOutDTO> response = funcionarioController.postFuncionario(dto);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(funcionarioOutDTO, response.getBody());
        verify(funcionarioConverter, times(1)).inDtoToEntity(dto);
        verify(funcionarioService, times(1)).save(funcionario);
        verify(funcionarioConverter, times(1)).entityToOutDTO(funcionarioCadastrado);
    }

    @Test
    void testPutFuncionario() {
        Integer idFuncionario = 1;
        FuncionarioInDTO funcionarioNovoDTO = new FuncionarioInDTO();
        Funcionario funcionarioNovo = new Funcionario();
        Funcionario funcionarioCadastrado = new Funcionario();
        FuncionarioOutDTO funcionarioAtualizado = new FuncionarioOutDTO();

        when(funcionarioConverter.inDtoToEntity(funcionarioNovoDTO)).thenReturn(funcionarioNovo);
        when(funcionarioService.update(funcionarioNovo, idFuncionario)).thenReturn(funcionarioCadastrado);
        when(funcionarioConverter.entityToOutDTO(funcionarioCadastrado)).thenReturn(funcionarioAtualizado);

        ResponseEntity<FuncionarioOutDTO> response = funcionarioController.putFuncionario(funcionarioNovoDTO, idFuncionario);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(funcionarioAtualizado, response.getBody());
        verify(funcionarioConverter, times(1)).inDtoToEntity(funcionarioNovoDTO);
        verify(funcionarioService, times(1)).update(funcionarioNovo, idFuncionario);
        verify(funcionarioConverter, times(1)).entityToOutDTO(funcionarioCadastrado);
    }

    @Test
    void testDeleteFuncionario() {
        Integer idFuncionario = 1;

        ResponseEntity<Object> response = funcionarioController.deleteFuncionario(idFuncionario);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        verify(funcionarioService, times(1)).delete(idFuncionario);
    }
}
