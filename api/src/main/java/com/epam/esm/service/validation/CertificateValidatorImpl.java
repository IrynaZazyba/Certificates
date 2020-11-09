package com.epam.esm.service.validation;

import com.epam.esm.dto.CertificateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class CertificateValidatorImpl implements Validator {

    private static final int NAME_SIZE = 45;
    private static final int DESCRIPTION_SIZE = 300;

    @Override
    public boolean supports(Class<?> aClass) {
        return CertificateDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CertificateDto certificateDto = (CertificateDto) o;
        if (certificateDto.getId() != null) {
            validateFields(certificateDto, errors);
        } else {
            ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
            ValidationUtils.rejectIfEmpty(errors, "description", "description.empty");
            validateFields(certificateDto, errors);
        }
    }

    private void validateFields(CertificateDto certificateDto, Errors errors) {
        if (!Objects.isNull(certificateDto.getDescription())&&
                certificateDto.getDescription().length() > DESCRIPTION_SIZE) {
            errors.rejectValue("description", "certificate.description.long");
        }

        if (!Objects.isNull(certificateDto.getName())
                &&certificateDto.getName().length() > NAME_SIZE) {
            errors.rejectValue("name", "certificate.name.long");
        }
    }

}
