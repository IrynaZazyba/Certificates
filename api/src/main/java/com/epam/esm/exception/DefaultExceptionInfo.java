package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DefaultExceptionInfo implements Serializable {

    private static final long serialVersionUID = -8987987432667111099L;
    private String errorMessage;
    private String errorCode;

}
