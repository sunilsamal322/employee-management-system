package com.spring.exception;

public class AdminCodeMismatchException extends RuntimeException{

    private String message;

    public AdminCodeMismatchException(String message)
    {
        super(message);
        this.message=message;
    }
}
