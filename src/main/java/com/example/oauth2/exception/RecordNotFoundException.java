package com.example.oauth2.exception;

public class RecordNotFoundException extends RuntimeException{
public RecordNotFoundException(String name){
    super(
            name
    );
}
}
