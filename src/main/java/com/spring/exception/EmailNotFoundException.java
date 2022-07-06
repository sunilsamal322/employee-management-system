package com.spring.exception;

public class EmailNotFoundException extends RuntimeException{

    private String message;

    public EmailNotFoundException(String message)
    {
        super(message);
        this.message=message;
    }
}
