package com.example.instagram.exception;

/**
 * 아이디 중복 예외
 */
public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
