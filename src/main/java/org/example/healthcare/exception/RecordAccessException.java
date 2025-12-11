package org.example.healthcare.exception;

public class RecordAccessException extends RuntimeException {
    public RecordAccessException(String message) {
        super(message);
    }
}

