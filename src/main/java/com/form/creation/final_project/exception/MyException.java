package com.form.creation.final_project.exception;

public class MyException extends RuntimeException {

    public MyException(String message) {
        super(message);
    }

    // Constructor with message and cause
    public MyException(String message, Throwable cause) {
        super(message, cause);
    }
}
