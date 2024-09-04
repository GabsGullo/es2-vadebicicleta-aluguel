package com.es2.vadebicicleta.es2.vadebicicleta.aluguel;


import com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception.*;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleNotFoundException() {
        NotFoundException exception = new NotFoundException("Recurso não encontrado", "404");
        ResponseEntity<ExceptionResponse> responseEntity = globalExceptionHandler.handleNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("404", responseEntity.getBody().getCodigo());
        assertEquals("Recurso não encontrado", responseEntity.getBody().getMensagem());
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Argumento ilegal");
        ResponseEntity<ExceptionResponse> responseEntity = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertEquals("422", responseEntity.getBody().getCodigo());
        assertEquals("Argumento ilegal", responseEntity.getBody().getMensagem());
    }

    @Test
    void testHandleMethodArgumentNotValidException() {
        Object objeto = new Object();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(objeto, "objeto");
        bindingResult.addError(new FieldError("objeto", "campo1", "Erro no campo 1"));
        bindingResult.addError(new FieldError("objeto", "campo2", "Erro no campo 2"));

        MethodParameter methodParameter = new MethodParameter(GlobalExceptionHandler.class.getMethods()[0], -1);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        ResponseEntity<List<ExceptionResponse>> responseEntity = globalExceptionHandler.handleMethodArgumentException(exception);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());

        List<ExceptionResponse> responseBody = responseEntity.getBody();
        assert responseBody != null;
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        // Verifique a lista manualmente para encontrar elementos esperados.
        List<ExceptionResponse> expectedResponses = Arrays.asList(
                new ExceptionResponse("Erro no campo 1", "422"),
                new ExceptionResponse("Erro no campo 2", "422")
        );

        // Verifica se todos os elementos esperados estão na resposta
        for (ExceptionResponse expected : expectedResponses) {
            boolean contains = responseBody.stream()
                    .anyMatch(response -> response.getMensagem().equals(expected.getMensagem()) &&
                            response.getCodigo().equals(expected.getCodigo()));
            assertTrue(contains, "Resposta não contém: " + expected);
        }
    }


    @Test
    void testHandleValidacaoException() {
        // Criando um BindingResult com erros de validação.
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objeto");
        bindingResult.addError(new FieldError("objeto", "campo", "Erro de validação"));

        // Criando uma exceção de validação com o BindingResult.
        ValidacaoException exception = new ValidacaoException(bindingResult);

        // Chamando o método handler para a exceção ValidacaoException.
        ResponseEntity<List<ExceptionResponse>> responseEntity = globalExceptionHandler.handleValidacaoException(exception);

        // Verificando o status da resposta.
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());

        // Verificando o conteúdo da resposta.
        List<ExceptionResponse> responseBody = responseEntity.getBody();
        assertEquals(1, responseBody.size());
        assertEquals("Erro de validação", responseBody.get(0).getMensagem());
        assertEquals("422", responseBody.get(0).getCodigo());
    }


    @Test
    void testHandleAluguelAtivoException() {
        AluguelAtivoException exception = new AluguelAtivoException("Aluguel ativo não pode ser cancelado");
        ResponseEntity<ExceptionResponse> responseEntity = globalExceptionHandler.handleAluguelAtivoException(exception);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("422", responseEntity.getBody().getCodigo());
        assertEquals("Aluguel ativo não pode ser cancelado", responseEntity.getBody().getMensagem());
    }

    @Test
    void testHandleDateTimeParseException() {
        DateTimeParseException exception = new DateTimeParseException("Data inválida", "data", 0);
        ResponseEntity<ExceptionResponse> responseEntity = globalExceptionHandler.handleDataInvalidaException(exception);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        assertEquals("422", responseEntity.getBody().getCodigo());
        assertEquals("Data Invalida", responseEntity.getBody().getMensagem());
    }
}