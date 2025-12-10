package com.project_agenda.agenda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailExistenteException extends RuntimeException{

    public EmailExistenteException(String message){
        super(message);
    }

}
