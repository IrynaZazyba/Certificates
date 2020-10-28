package com.epam.esm.service.validation.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.service.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CertificateValidatorImpl implements Validator<CertificateDto> {

    private static final int NAME_SIZE = 45;
    private static final int DESCRIPTION_SIZE = 300;

    public boolean validate(CertificateDto certificateDto) {
        if (!Objects.isNull(certificateDto.getDescription()) && !Objects.isNull(certificateDto.getName())) {
            return certificateDto.getName().length() < NAME_SIZE
                    && certificateDto.getDescription().length() < DESCRIPTION_SIZE;
        }

        if (!Objects.isNull(certificateDto.getDescription()) && Objects.isNull(certificateDto.getName())) {
            return certificateDto.getDescription().length() < DESCRIPTION_SIZE;
        }

        if (Objects.isNull(certificateDto.getDescription()) && !Objects.isNull(certificateDto.getName())) {
            return certificateDto.getName().length() < NAME_SIZE;
        }

        return Objects.isNull(certificateDto.getDescription()) && Objects.isNull(certificateDto.getName());
    }

}
