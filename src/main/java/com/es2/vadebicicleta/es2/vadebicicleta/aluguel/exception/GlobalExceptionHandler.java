package com.es2.vadebicicleta.es2.vadebicicleta.aluguel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), ex.getCodigo());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentException(MethodArgumentTypeMismatchException ex) {
        String mensagem;
        if(ex.getName().equals("idCiclista"))
            mensagem = "id do ciclista invalida";
        else
            mensagem = ex.getMessage();

        ExceptionResponse exceptionResponse = new ExceptionResponse(mensagem, "422");
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exceptionResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), "422");
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionResponse>> handleMethodArgumentException(MethodArgumentNotValidException ex) {
        List<ExceptionResponse> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(new ExceptionResponse(errorMessage, "422"));
        });

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<List<ExceptionResponse>> handleValidacaoException(ValidacaoException ex) {
        List<ExceptionResponse> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(new ExceptionResponse(errorMessage, "422"));
        });

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
    }

    @ExceptionHandler(AluguelAtivoException.class)
    public ResponseEntity<ExceptionResponse> handleAluguelAtivoException(AluguelAtivoException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), "422");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ExceptionResponse> handleDataInvalidaException(DateTimeParseException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse("Data Invalida", "422");
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exceptionResponse);
    }
}
