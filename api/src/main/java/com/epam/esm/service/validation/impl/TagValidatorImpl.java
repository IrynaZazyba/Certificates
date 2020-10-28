package com.epam.esm.service.validation.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class TagValidatorImpl implements Validator<TagDto> {

    private static final int NAME_SIZE = 45;

    public boolean validate(TagDto tagDto) {
        return tagDto.getName().length() < NAME_SIZE;
    }

}
