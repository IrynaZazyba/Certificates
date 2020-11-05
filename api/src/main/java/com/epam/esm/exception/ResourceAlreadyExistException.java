package com.epam.esm.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ResourceAlreadyExistException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -9092309524772575762L;
    private Long id;

    public ResourceAlreadyExistException(String message) {
        super(message);
    }

    public ResourceAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAlreadyExistException(Throwable cause) {
        super(cause);
    }

    public ResourceAlreadyExistException(String message, Long id) {
        super(message);
        this.id = id;
    }

}
