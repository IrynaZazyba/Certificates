package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.FilterDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface CertificateService {

    CertificateDto getOne(Long id);

    List<CertificateDto> getAll();

    CertificateDto create(CertificateDto certificate);

    void delete(Long id);

    List<CertificateDto> filter(FilterDto filterDto);

    void update(CertificateDto certificateDto);

    void linkTag(Long certificateId, Long tagId);
}
