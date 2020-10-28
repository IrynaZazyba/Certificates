package com.epam.esm.service.validation.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.service.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class CertificateValidatorImpl implements Validator<CertificateDto> {

    private static final int NAME_SIZE = 45;
    private static final int DESCRIPTION_SIZE = 300;

    public boolean validate(CertificateDto certificateDto) {
        if (certificateDto.getDescription() != null && certificateDto.getName() != null) {
            return certificateDto.getName().length() < NAME_SIZE
                    && certificateDto.getDescription().length() < DESCRIPTION_SIZE;
        }

        if (certificateDto.getDescription() != null && certificateDto.getName() == null) {
            return certificateDto.getDescription().length() < DESCRIPTION_SIZE;
        }

        if (certificateDto.getDescription() == null && certificateDto.getName() != null) {
            return certificateDto.getName().length() < NAME_SIZE;
        }

        return certificateDto.getDescription() == null && certificateDto.getName() == null;
    }

}
