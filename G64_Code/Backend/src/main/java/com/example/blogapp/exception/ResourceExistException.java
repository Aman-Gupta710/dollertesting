package com.example.blogapp.exception;

public class ResourceExistException extends RuntimeException {
    String resourceName;
    String fieldName;
    String fieldValue;
    public ResourceExistException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s already exist with %s : %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
