package com.shophub.AuthService.exceptions;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String wrongPassword) {
        super(wrongPassword);
    }
}
