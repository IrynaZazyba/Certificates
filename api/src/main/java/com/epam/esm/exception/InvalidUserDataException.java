package com.epam.esm.exception;

import java.io.Serializable;

public class InvalidUserDataException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 8816038030900480150L;

    public InvalidUserDataException() {
    }

    public InvalidUserDataException(String message) {
        super(message);
    }

    public InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserDataException(Throwable cause) {
        super(cause);
    }

}
