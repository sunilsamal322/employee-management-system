package com.spring.exception;

public class EmailAlreadyExistException extends RuntimeException{

    private String message;

    public EmailAlreadyExistException(String message)
    {
        super(message);
        this.message=message;
    }
}
