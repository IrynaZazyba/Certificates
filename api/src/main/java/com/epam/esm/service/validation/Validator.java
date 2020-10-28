package com.epam.esm.service.validation;

public interface Validator<T> {

    boolean validate(T t);
}
