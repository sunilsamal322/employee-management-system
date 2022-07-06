package com.spring.exception;

public class ResourceNotFoundException extends RuntimeException{

    private String resourceName;
    private String resourceField;
    private Integer resourceValue;

    public ResourceNotFoundException(String resourceName,String resourceField,Integer resourceValue)
    {
        super(String.format("%s not found with %s : %d",resourceName,resourceField,resourceValue));
        this.resourceName=resourceName;
        this.resourceField=resourceField;
        this.resourceValue=resourceValue;
    }
}
