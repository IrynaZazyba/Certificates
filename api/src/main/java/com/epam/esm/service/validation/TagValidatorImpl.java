package com.epam.esm.service.validation;

import com.epam.esm.dto.TagDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TagValidatorImpl implements Validator {

    private static final int NAME_SIZE = 45;

    @Override
    public boolean supports(Class<?> aClass) {
        return TagDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
        TagDto tag = (TagDto) o;
        if (tag.getName().length() > NAME_SIZE) {
            errors.rejectValue("name", "tag.name.long");
        }
    }
}
