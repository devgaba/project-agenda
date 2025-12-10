package com.project_agenda.agenda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TratamentoGlobalException {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Object> capturarExcecaoRecursoInexistente(RecursoNaoEncontradoException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailExistenteException.class)
    public ResponseEntity<Object> capturarExcecaoEmailExistente(EmailExistenteException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
