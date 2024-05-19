package com.vanky.chat.common.exception;

/**
 * @author vanky
 * @version 1.0
 * @since 1.0
 */
public class BaseException extends RuntimeException{

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }
}
