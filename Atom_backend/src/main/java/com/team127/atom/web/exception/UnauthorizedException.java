package com.team127.atom.web.exception;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(){
        super();
    }
}