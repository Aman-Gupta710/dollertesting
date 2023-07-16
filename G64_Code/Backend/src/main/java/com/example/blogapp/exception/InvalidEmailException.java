package com.example.blogapp.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidEmailException extends RuntimeException{
    String message;
    public InvalidEmailException(String message){
        super(message);
        this.message = message;
    }
}
