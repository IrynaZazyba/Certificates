package com.epam.esm.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.BindingResult;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class InvalidUserDataException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 8816038030900480150L;
    private BindingResult result;

     public InvalidUserDataException(String message, BindingResult bindingResult) {
        super(message);
        this.result = bindingResult;
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
